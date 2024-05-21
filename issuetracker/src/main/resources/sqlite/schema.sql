DROP TABLE IF EXISTS team_member;
DROP TABLE IF EXISTS team;
DROP TABLE IF EXISTS account;
DROP TABLE IF EXISTS issue;

CREATE TABLE account
(
    id           text PRIMARY KEY,
    username     text UNIQUE NOT NULL,
    first_name   text,
    last_name    text,
    date_created text
);

CREATE TABLE team
(
    id           text PRIMARY KEY,
    name         text,
    date_created text NOT NULL
);

CREATE TABLE team_member
(
    id         integer PRIMARY KEY,
    team_id    text REFERENCES team (id) ON DELETE CASCADE ON UPDATE CASCADE    NOT NULL,
    account_id text REFERENCES account (id) ON DELETE CASCADE ON UPDATE CASCADE NOT NULL
);

CREATE TABLE issue
(
    id          integer PRIMARY KEY,
    title       text NOT NULL,
    description text,
    created_at  text NOT NULL,
    due_date    text,
    due_date_time_zone text,
    closed      integer,
    CONSTRAINT CHK_due_date_has_time_zone CHECK (
        (due_date NOT NULL AND due_date_time_zone NOT NULL)
        OR
        (due_date IS NULL AND due_date_time_zone IS NULL)
    )
);
