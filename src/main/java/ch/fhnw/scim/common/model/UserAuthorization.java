package ch.fhnw.scim.common.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity(name = "user_authorization")
public class UserAuthorization {

	@Id
	@GeneratedValue
	private int id;

	private String role;

	@ManyToOne(fetch = FetchType.EAGER)
	private ch.fhnw.scim.common.model.System system;

	@Column(name = "user_id")
	private int user_id;


	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRole() {
		return this.role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public ch.fhnw.scim.common.model.System getSystem() {
		return this.system;
	}

	public void setSystem(ch.fhnw.scim.common.model.System system) {
		this.system = system;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}


}
