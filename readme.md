Description
------------

The system consists of 3 modules.
Common: Contains JPA access, entity classes, validation classes and logging.
Admin: Rest services for Admin UI access.
Scim: Rest services to expose Scim 2 functionality, to/from scim mapping, etc.

The database is structured as 3 tables:
user: User details 
system: System details
user_authorization: Defines user/system access and autorization roles (Admin/User)

Scim:
URL to Scim: http://localhost:8080/admin/
URL to look up default user as Scim: http://localhost:8080/admin/scim/v2/d9a5a77b-be41-47cb-8663-c95830ce173e

Method interface:
Get (http://localhost:8080/admin/scim/v2/): Fetches all users
Get (http://localhost:8080/admin/scim/v2/#scimId#): Fetches specific user
Put (http://localhost:8080/admin/scim/v2/#scimId#): Replaces specific user (delete/insert)
Patch (http://localhost:8080/admin/scim/v2/#scimId#): Updates specific user (update)
Post (http://localhost:8080/admin/scim/v2/): Creates a new user (create)
Delete (http://localhost:8080/admin/scim/v2/#scimId#): Deletes a specific user (delete)

Database config for MySQL as in application.properties
URL: jdbc:mysql://localhost:3306/scim
Username: root
Password: root


Database-Script
----------------------

CREATE DATABASE scim;

CREATE TABLE system (
  id int(11) NOT NULL AUTO_INCREMENT,
  name varchar(45) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE user (
  id int(11) NOT NULL AUTO_INCREMENT,
  first_name varchar(45) NOT NULL,
  password varchar(45) DEFAULT NULL,
  email varchar(45) DEFAULT NULL,
  created datetime DEFAULT NULL,
  active int(1) DEFAULT NULL,
  role varchar(45) DEFAULT NULL,
  last_name varchar(45) DEFAULT NULL,
  birthday datetime DEFAULT NULL,
  gender varchar(45) DEFAULT NULL,
  scim_id varchar(45) DEFAULT NULL,
  last_modified datetime DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE user_authorization (
  id int(11) NOT NULL AUTO_INCREMENT,
  user_id int(11) NOT NULL,
  system_id int(11) NOT NULL,
  role varchar(45) NOT NULL,
  PRIMARY KEY (id),
  KEY systemFK_idx (system_id),
  KEY userFK_idx (user_id),
  CONSTRAINT systemFK FOREIGN KEY (system_id) REFERENCES system (id) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT userFK FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB;


INSERT INTO user(id, first_name, password, email, created, active, role, last_name, birthday, gender, scim_id, last_modified)
VALUES ('1', ‘Meng Long’, ‘admin’, ‘meng.chy@students.fhnw.ch’, NULL, '1', NULL, 'Nielsen', '1980-06-28 02:00:00', 'MALE', 'd9a5a77b-be41-47cb-8663-c95830ce173e', NULL);
VALUES ('2', 'Kajan', '66', 'kajan.vija@students.fhnw.ch', '2017-07-25 21:39:16', '1', 'USER', 'Vija', '1994-01-01 01:00:00', 'MALE', '8dc4dba3-92af-4ab0-a068-aa1a04bad604', NULL);


INSERT INTO system (id, name) VALUES ('1', 'System 1');
INSERT INTO system (id, name) VALUES ('2', 'System 1');

INSERT INTO user_authorization (id, user_id, system_id, role) VALUES ('3', '2', '1', 'USER');







