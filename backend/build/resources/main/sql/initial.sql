-- psql -U postgres

SET client_encoding TO 'UTF8';

CREATE DATABASE playme;

CREATE ROLE dbadmin WITH PASSWORD 'adminpass';
ALTER ROLE dbadmin WITH LOGIN;

GRANT ALL PRIVILEGES ON DATABASE playme TO dbadmin;

CREATE ROLE dbuser WITH PASSWORD 'userpass';
ALTER ROLE dbuser WITH LOGIN;

-- psql -U postgres -d playme

GRANT SELECT, UPDATE, DELETE, INSERT ON ALL TABLES IN SCHEMA public TO dbuser;
