package ch.fhnw.scim.scim.mapper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ch.fhnw.scim.common.exception.BusinessException;
import ch.fhnw.scim.common.model.System;
import ch.fhnw.scim.common.model.UserAuthorization;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.unboundid.scim2.common.exceptions.ResourceNotFoundException;
import com.unboundid.scim2.common.types.Email;
import com.unboundid.scim2.common.types.Meta;
import com.unboundid.scim2.common.types.Name;
import com.unboundid.scim2.common.types.Role;
import com.unboundid.scim2.common.types.UserResource;

import ch.fhnw.scim.common.business.SystemBusiness;
import ch.fhnw.scim.common.model.User;
import ch.fhnw.scim.scim.model.ScimUser;
import ch.fhnw.scim.scim.util.ServerUtils;

@Component
public class ScimMapper {

	@Autowired
	private SystemBusiness systemBusiness;

	@Value("${server.contextPath}")
	private String contextPath;

	@Value("${server.port}")
	private String serverPort;

	public ScimUser toSCIM(User user) throws Exception {
		String prefix = "";
		ScimUser scimUser = new ScimUser();

		scimUser.setInternalId(user.getId());
		scimUser.setId(user.getScimId());
		scimUser.setUserName(user.getEmail());
		scimUser.setPassword(user.getPassword());
		scimUser.setActive(user.isActive());

		scimUser.setMeta(new Meta());
		scimUser.getMeta().setCreated(asCalendar(user.getCreated()));
		scimUser.getMeta().setLastModified(asCalendar(user.getLastModified()));
		scimUser.getMeta().setResourceType("User");
		scimUser.getMeta().setLocation(ServerUtils.buildLocation(user.getScimId(), serverPort, contextPath));

		if (!StringUtils.isEmpty(user.getGender())) {
			prefix = user.getGender().equalsIgnoreCase("male") ? "Mr " : "Mrs ";

		}

		scimUser.setName(new Name());
		scimUser.getName().setHonorificPrefix(prefix);
		scimUser.getName().setGivenName(user.getFirstName());
		scimUser.getName().setFamilyName(user.getLastName());
		scimUser.getName().setFormatted(prefix + user.getFirstName() + " " + user.getLastName());

		if (!StringUtils.isEmpty(user.getEmail())) {
			Email email = new Email();
			email.setValue(user.getEmail());
			email.setPrimary(true);
			email.setType("work");

			scimUser.setEmails(new ArrayList<Email>());
			scimUser.getEmails().add(email);
		}

		if (!StringUtils.isEmpty(user.getRole())) {
			Role role = new Role();
			role.setPrimary(true);
			role.setValue(user.getRole());

			scimUser.setRoles(new ArrayList<Role>());
			scimUser.getRoles().add(role);
		}

		for (UserAuthorization auth : user.getUserAuthorizations()) {
			scimUser.addAuth(auth.getSystem(), auth.getRole());
		}

		scimUser.setGender(user.getGender());
		scimUser.setBirthday(user.getBirthday());
		scimUser.initExtensionValues();
		return scimUser;
	}

	public User fromSCIM(ScimUser scimUser, User user) throws ResourceNotFoundException, BusinessException {
		user.setScimId(scimUser.getId());
		user.setPassword(scimUser.getPassword());
		user.setActive(scimUser.getActive() != null ? scimUser.getActive() : false);

		user.setBirthday(scimUser.getBirthday());
		user.setGender(scimUser.getGender());

		if (scimUser.getMeta() != null && scimUser.getMeta().getCreated() != null) {
			user.setCreated(scimUser.getMeta().getCreated().getTime());
		}

		if (scimUser.getName() != null) {
			user.setFirstName(scimUser.getName().getGivenName());
			user.setLastName(scimUser.getName().getFamilyName());
		}

		if (scimUser.getEmails() != null && scimUser.getEmails().size() > 0) {
			user.setEmail(scimUser.getEmails().get(0).getValue());
		}

		if (scimUser.getRoles() != null && scimUser.getRoles().size() > 0) {
			String role = scimUser.getRoles().get(0).getValue();
			if (!"admin".equalsIgnoreCase(role) && !"user".equalsIgnoreCase(role)) {
				throw new BusinessException("Role is not allowed");
			}
			user.setRole(scimUser.getRoles().get(0).getValue());
		}

		return user;
	}

	public List<UserResource> toSCIM(List<User> users) throws Exception {
		List<UserResource> scimUsers = new ArrayList<UserResource>();
		for(User user : users) {
			scimUsers.add(toSCIM(user));
		}
		return scimUsers;
	}

	public List<User> fromScim(List<ScimUser> scimUsers) throws ResourceNotFoundException, BusinessException {
		List<User> users = new ArrayList<User>();
		for(ScimUser scimUser : scimUsers) {
			users.add(fromSCIM(scimUser, new User()));
		}
		return users;
	}

	public void convertUserAuthorizations(User user, ScimUser scimUser) throws ResourceNotFoundException {
		user.setUserAuthorizations(new ArrayList<UserAuthorization>());
		if (scimUser.getAuthorizations().size() > 0) {
			user.setUserAuthorizations(new ArrayList<UserAuthorization>());
			for (JsonNode node : scimUser.getAuthorizations()) {
				UserAuthorization auth = new UserAuthorization();
				auth.setUser_id(user.getId());
				auth.setSystem(getSystem(node));
				auth.setRole(node.get(ScimUser.ROLE).asText());
				user.getUserAuthorizations().add(auth);
			}
		}
	}

	private System getSystem(JsonNode node) throws ResourceNotFoundException {
		String systemName = node.get(ScimUser.SYSTEM_NAME).asText();
		if (StringUtils.isEmpty(systemName)) {
			throw new ResourceNotFoundException("System name cannot be empty");
		}

		System system = systemBusiness.findByName(systemName);
		if (system == null) {
			throw new ResourceNotFoundException("Could not find system: " + systemName);
		}

		return system;
	}

	private Calendar asCalendar(Date date) {
		if (date == null) {
			return null;
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar;
	}

}