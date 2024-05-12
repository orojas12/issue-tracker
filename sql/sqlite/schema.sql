DROP TABLE IF EXISTS team_member;
DROP TABLE IF EXISTS team;
DROP TABLE IF EXISTS account;

CREATE TABLE account (
	id text PRIMARY KEY,
	username text UNIQUE NOT NULL,
	first_name text,
	last_name text,
	date_created text
);

CREATE TABLE team (
	id text PRIMARY KEY,
	name text,
	date_created text NOT NULL
);

CREATE TABLE team_member (
	id integer PRIMARY KEY AUTOINCREMENT,
	team_id text REFERENCES team (id) 
	    ON DELETE CASCADE ON UPDATE CASCADE NOT NULL,
	account_id text REFERENCES account (id)
	    ON DELETE CASCADE ON UPDATE CASCADE NOT NULL
);
