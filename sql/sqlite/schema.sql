CREATE TABLE IF NOT EXISTS account (
	id text PRIMARY KEY,
	username text UNIQUE NOT NULL,
	first_name text,
	last_name text,
	date_created text
);

CREATE TABLE IF NOT EXISTS team (
	id text PRIMARY KEY,
	name text,
	date_created text NOT NULL
);

CREATE TABLE IF NOT EXISTS team_member (
	id integer PRIMARY KEY AUTOINCREMENT,
	team_id text REFERENCES team (id) NOT NULL
	    ON DELETE CASCADE ON UPDATE CASCADE,
	account_id text REFERENCES account (id) NOT NULL
	    ON DELETE CASCADE ON UPDATE CASCADE
);
