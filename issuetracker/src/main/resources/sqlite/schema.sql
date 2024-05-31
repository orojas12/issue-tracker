DROP TABLE IF EXISTS issue;
DROP TABLE IF EXISTS contributor;
DROP TABLE IF EXISTS project;
DROP TABLE IF EXISTS account;

CREATE TABLE account
(
    id          text PRIMARY KEY,
    username    text UNIQUE NOT NULL,
    first_name  text,
    last_name   text,
    created_at  text
);

CREATE TABLE project
(
    id          text PRIMARY KEY,
    name        text,
    created_at  text NOT NULL
);

CREATE TABLE contributor
(
    id          integer PRIMARY KEY,
    project_id  text REFERENCES project (id) ON DELETE CASCADE ON UPDATE CASCADE NOT NULL,
    account_id  text REFERENCES account (id) ON DELETE CASCADE ON UPDATE CASCADE NOT NULL
);

CREATE TABLE issue
(
    id          integer PRIMARY KEY,
    title       text NOT NULL,
    description text,
    created_at  text NOT NULL,
    closed      integer,
    project_id  text REFERENCES project (id) ON DELETE CASCADE ON UPDATE CASCADE NOT NULL
);
