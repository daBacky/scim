package ch.fhnw.scim.scim.handler;

import java.util.List;

import ch.fhnw.scim.common.business.LogBusiness;
import ch.fhnw.scim.common.exception.BusinessException;
import ch.fhnw.scim.common.model.User;
import ch.fhnw.scim.scim.mapper.ScimMapper;
import ch.fhnw.scim.scim.model.ScimUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ch.fhnw.scim.common.business.UserBusiness;

import com.unboundid.scim2.common.ScimResource;
import com.unboundid.scim2.common.exceptions.PreconditionFailedException;
import com.unboundid.scim2.common.exceptions.ResourceNotFoundException;
import com.unboundid.scim2.common.exceptions.ScimException;
import com.unboundid.scim2.common.exceptions.ServerErrorException;

@Component
public class ScimHandler {

	@Autowired
	private UserBusiness userBusiness;

	@Autowired
	private LogBusiness log;

	@Autowired
	private ScimMapper mapper;

	public void delete(String scimId) throws ScimException {
		User user = userBusiness.findByScimId(scimId);
		if (user == null) {
			throw new ResourceNotFoundException("No resource with ID: " + scimId);
		}

		userBusiness.delete(user.getId());
	}

	public ScimResource create(ScimUser scimUser) throws ScimException {
		try {
			User user = mapper.fromSCIM(scimUser, new User());
			mapper.convertUserAuthorizations(user, scimUser);
			user = userBusiness.save(user);
			return mapper.toSCIM(user);
		} catch(BusinessException b) {
			throw new PreconditionFailedException(b.getMessage());
		} catch (Exception e) {
			log.error("Could not create user. Message: " + e.getMessage());
			throw new ServerErrorException("Could not create user");
		}
	}

	public ScimResource modify(String scimId, ScimUser scimUser) throws ScimException {
		try {
			User user = userBusiness.findByScimId(scimId);
			if (user == null) {
				throw new ResourceNotFoundException("No resource found with Id: " + scimId);
			}

			scimUser.setId(scimId);
			user = mapper.fromSCIM(scimUser, user);
			mapper.convertUserAuthorizations(user, scimUser);

			user = userBusiness.save(user);
			return mapper.toSCIM(user);

		} catch(BusinessException b) {
			throw new PreconditionFailedException(b.getMessage());
		} catch(ResourceNotFoundException rnf) {
			throw rnf;
		} catch (Exception e) {
			log.error("Could not modify user. Message: " + e.getMessage());
			throw new ServerErrorException("Could not update the user with id: " + scimId);
		}
	}

	public ScimResource replace(String scimId, ScimUser scimUser) throws ScimException {
		try {
			User user = userBusiness.findByScimId(scimId);
			if (user == null) {
				throw new ResourceNotFoundException("No resource found with Id: " + scimId);
			}

			user = mapper.fromSCIM(scimUser, user);

			// Replace the entire user object (Delete/insert)
			userBusiness.delete(user.getId());

			user.setId(0);
			user.setCreated(null);
			user.setUserAuthorizations(null);

			user = userBusiness.save(user);

			// Need to get the ID of the new user and add it to the authorization object. Otherwise we run into an SQL exception.
			mapper.convertUserAuthorizations(user, scimUser);
			user = userBusiness.save(user);

			return mapper.toSCIM(user);

		} catch(BusinessException b) {
			throw new PreconditionFailedException(b.getMessage());
		} catch(ResourceNotFoundException rnf) {
			throw rnf;
		} catch (Exception e) {
			log.error("Could not replace user. Message: " + e.getMessage());
			throw new ServerErrorException("Could not replace the user with id: " + scimId);
		}
	}

	public ScimResource retrieve(String scimId) throws ScimException {
		try {
			User user = userBusiness.findByScimId(scimId);

			if (user == null) {
				throw new ResourceNotFoundException("No resource found with Id: " + scimId);
			}

			return mapper.toSCIM(user);

		} catch(ResourceNotFoundException rnf) {
			throw rnf;
		} catch (Exception e) {
			log.error("Could not retrieve user. Message: " + e.getMessage());
			throw new ServerErrorException("Could not retrieve the requested resource: " + scimId);
		}
	}

	public List search() throws ScimException {
		try {
			List<User> users = userBusiness.findAll();
			return mapper.toSCIM(users);
		} catch (Exception e) {
			log.error("Search failed. Message: " + e.getMessage());
			throw new ServerErrorException("Something went wrong during search");
		}
	}
}
