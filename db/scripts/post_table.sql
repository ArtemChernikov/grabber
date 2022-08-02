CREATE TABLE posts IF NOT EXISTS(
id serial PRIMARY KEY,
name text,
text text,
link text UNIQUE,
created timestamp
);