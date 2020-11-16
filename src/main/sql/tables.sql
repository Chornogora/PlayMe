-- psql -U dbadmin -d playme

CREATE TABLE roles(
	id varchar(36) NOT NULL PRIMARY KEY,
	name varchar(64) NOT NULL UNIQUE
);

CREATE TABLE statuses(
	id varchar(36) NOT NULL PRIMARY KEY,
	name varchar(64) NOT NULL UNIQUE
);

CREATE TABLE users(
	user_id varchar(36) NOT NULL PRIMARY KEY,
	login varchar(64) NOT NULL UNIQUE,
	password varchar(64) NOT NULL,
    email varchar(320) NOT NULL UNIQUE,
	birthdate date NOT NULL,
	first_name varchar(64) NOT NULL,
	last_name varchar(64) NOT NULL,
	role_id varchar(36) NOT NULL,
	status_id varchar(36) NOT NULL,
	creation_date date not null,
	modification_date date not null,
	FOREIGN KEY(role_id) REFERENCES roles(id),
	FOREIGN KEY(status_id) REFERENCES statuses(id)
);

CREATE TABLE musicians(
	id varchar(36) NOT NULL PRIMARY KEY,
	nickname varchar(64) NOT NULL,
	user_id varchar(36) NOT NULL UNIQUE,
	band_id varchar(36)
);

CREATE TABLE bands(
	id varchar(36) NOT NULL PRIMARY KEY,
	name varchar(64) NOT NULL UNIQUE,
	creation_date date NOT NULL,
	leader_id varchar(36) NOT NULL,
	FOREIGN KEY(leader_id) REFERENCES users(id)
);

ALTER TABLE musicians ADD FOREIGN KEY(band_id) REFERENCES bands(id);

CREATE TABLE skills(
	id varchar(36) NOT NULL PRIMARY KEY,
	name varchar(64) NOT NULL UNIQUE
);

CREATE TABLE skills_musicians(
	skill_id varchar(36) NOT NULL,
	musician_id varchar(36) NOT NULL,
	skill_level varchar(64) NOT NULL,
	CONSTRAINT skills_musicians_primary_key PRIMARY KEY (skill_id, musician_id),
	FOREIGN KEY(skill_id) REFERENCES skills(id),
	FOREIGN KEY(musician_id) REFERENCES musicians(id)
);

CREATE TABLE repetitions(
	id varchar(36) NOT NULL PRIMARY KEY,
	start_datetime date NOT NULL,
	finish_datetime date,
	description varchar,
	creator_id varchar(36) NOT NULL,
	FOREIGN KEY(creator_id) REFERENCES musicians(id)
);

CREATE TABLE repetitions_musicians(
	repetition_id varchar(36) NOT NULL,
	musician_id varchar(36) NOT NULL,
	approved boolean NOT NULL DEFAULT FALSE,
	FOREIGN KEY(repetition_id) REFERENCES repetitions(id),
	FOREIGN KEY(musician_id) REFERENCES musicians(id)
);