package ch.fhnw.scim.scim.model;

import java.util.Date;

import ch.fhnw.scim.common.model.System;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.unboundid.scim2.common.annotations.Schema;
import com.unboundid.scim2.common.exceptions.ScimException;
import com.unboundid.scim2.common.types.UserResource;

@Schema(description = "Class to represent custom fhnw attributes", id = "urn:ch.fhnw.scim.model.User", name = "User")
@JsonIgnoreProperties(value = { "internalId", "gender", "birthday", "internalObjNode" })
public class ScimUser extends UserResource {

	public static final String ID = "id";
	public static final String ROLE = "role";
	public static final String SYSTEM_ID = "systemId";
	public static final String SYSTEM_NAME = "systemName";
	public static final String AUTH_LIST = "authorizations";
	public static final String INTERNAL = "internal";
	public static final String BIRTHDAY = "birthday";
	public static final String GENDER = "gender";

	private ObjectNode internalObjNode = null;
	private ArrayNode internal = null;
	private ArrayNode authorizations = null;

	public ScimUser() {
	}

	public int getInternalId() {
		if (getInternal().size() > 0) {
			return getInternal().get(0).get(ID).asInt();
		} else {
			return 0;
		}
	}

	public void setInternalId(int internalId) {
		getInternalObjNode().put(ID, internalId);
	}

	public void addAuth(System system, String role) throws ScimException {
		ObjectNode node = new ObjectMapper().createObjectNode();
		node.put(SYSTEM_ID, system.getId());
		node.put(SYSTEM_NAME, system.getName());
		node.put(ROLE, role);
		getAuthorizations().add(node);
	}

	public Date getBirthday() {
		if (getInternal().size() > 0 && getInternal().get(0).get(BIRTHDAY) != null) {
			return new Date(getInternal().get(0).get(BIRTHDAY).asLong());
		} else {
			return null;
		}
	}

	public void setBirthday(Date birthday) {
		if (birthday != null) {
			getInternalObjNode().put(BIRTHDAY, birthday.getTime());
		}
	}

	public String getGender() {
		if (getInternal().size() > 0) {
			return getInternal().get(0).get(GENDER).asText();
		} else {
			return null;
		}
	}

	public void setGender(String gender) {
		getInternalObjNode().put(GENDER, gender);
	}

	public void initExtensionValues() throws ScimException {
		getInternal().add(getInternalObjNode());
		addExtensionValue(INTERNAL, getInternal());
		addExtensionValue(AUTH_LIST, getAuthorizations());
	}

	public ArrayNode getAuthorizations() {
		if (authorizations == null) {
			authorizations = new ArrayNode(null);
		}
		return authorizations;
	}

	public ArrayNode getInternal() {
		if (internal == null) {
			internal = new ArrayNode(null);
		}
		return internal;
	}

	private ObjectNode getInternalObjNode() {
		if (internalObjNode == null) {
			internalObjNode = new ObjectMapper().createObjectNode();
		}
		return internalObjNode;
	}
}