-- psql -U dbadmin -d playme

CREATE TABLE roles(
	role_id varchar(36) NOT NULL PRIMARY KEY,
	role_name varchar(64) NOT NULL UNIQUE
);

CREATE TABLE statuses(
	status_id varchar(36) NOT NULL PRIMARY KEY,
	status_name varchar(64) NOT NULL UNIQUE
);

CREATE TABLE users(
	user_id varchar(36) NOT NULL PRIMARY KEY,
	user_login varchar(64) NOT NULL UNIQUE,
	user_password varchar(64) NOT NULL,
	user_email varchar(320) NOT NULL UNIQUE,
	user_birthdate date NOT NULL,
	user_name varchar(64) NOT NULL,
	role_id varchar(36) NOT NULL,
	status_id varchar(36) NOT NULL,
	creation_date date not null,
	modification_date date not null,
	FOREIGN KEY(role_id) REFERENCES roles(role_id),
	FOREIGN KEY(status_id) REFERENCES statuses(status_id)
);

CREATE TABLE musicians(
	musician_id varchar(36) NOT NULL PRIMARY KEY,
	musician_nickname varchar(64) NOT NULL,
	user_id varchar(36) NOT NULL UNIQUE,
	band_id varchar(36)
);

CREATE TABLE bands(
	band_id varchar(36) NOT NULL PRIMARY KEY,
	band_name varchar(64) NOT NULL UNIQUE,
	band_creation_date date NOT NULL,
	band_leader_id varchar(36) NOT NULL,
	FOREIGN KEY(band_leader_id) REFERENCES users(user_id)
);

ALTER TABLE musicians ADD FOREIGN KEY(band_id) REFERENCES bands(band_id);

CREATE TABLE skills(
	skill_id varchar(36) NOT NULL PRIMARY KEY,
	role_name varchar(64) NOT NULL UNIQUE
);

CREATE TABLE skills_musicians(
	skill_id varchar(36) NOT NULL,
	musician_id varchar(36) NOT NULL,
	skill_level varchar(64) NOT NULL,
	CONSTRAINT skills_musicians_primary_key PRIMARY KEY (skill_id, musician_id),
	FOREIGN KEY(skill_id) REFERENCES skills(skill_id),
	FOREIGN KEY(musician_id) REFERENCES musicians(musician_id)
);

CREATE TABLE repetitions(
	repetition_id varchar(36) NOT NULL PRIMARY KEY,
	repetition_start_datetime date NOT NULL,
	repetition_finish_datetime date,
	repetition_description varchar,
	repetition_creator_id varchar(36) NOT NULL,
	FOREIGN KEY(repetition_creator_id) REFERENCES musicians(musician_id)
);

CREATE TABLE repetitions_musicians(
	repetition_id varchar(36) NOT NULL,
	musician_id varchar(36) NOT NULL,
	approved boolean NOT NULL DEFAULT FALSE,
	FOREIGN KEY(repetition_id) REFERENCES repetitions(repetition_id),
	FOREIGN KEY(musician_id) REFERENCES musicians(musician_id)
);