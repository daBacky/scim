package ch.fhnw.scim.common.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity(name = "user")
public class User {

	@Id
	@GeneratedValue
	private int id;

	private String scimId;

	@JsonFormat(pattern="yyyy-MM-dd")
	@Temporal(TemporalType.TIMESTAMP)
	private Date created;

	@JsonFormat(pattern="yyyy-MM-dd")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastModified;

	@JsonFormat(pattern="yyyy-MM-dd")
	@Temporal(TemporalType.TIMESTAMP)
	private Date birthday;

	private String firstName;

	private String lastName;

	private String gender;

	private String email;

	private boolean active;

	private String password;

	private String role;

	@OneToMany(fetch=FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "user_id", orphanRemoval=true)
	private List<UserAuthorization> userAuthorizations;

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isActive() {
		return this.active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Date getCreated() {
		return this.created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getLastModified() {
		return this.lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Date getBirthday() {
		return this.birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return this.role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public List<UserAuthorization> getUserAuthorizations() {
		return userAuthorizations;
	}

	public void setUserAuthorizations(List<UserAuthorization> userAuthorizations) {
		this.userAuthorizations = userAuthorizations;
	}

	public String getScimId() {
		return scimId;
	}

	public void setScimId(String scimId) {
		this.scimId = scimId;
	}

}
