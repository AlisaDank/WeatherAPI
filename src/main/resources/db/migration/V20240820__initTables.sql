CREATE SCHEMA if not exists weatherdb;

CREATE TABLE if not exists users
(
    id       SERIAL PRIMARY KEY,
    login    VARCHAR UNIQUE NOT NULL CHECK ( length(login) > 2 ),
    password VARCHAR        NOT NULL CHECK ( length(password) > 2 )
);

CREATE TABLE if not exists locations
(
    id        SERIAL PRIMARY KEY,
    user_id   INT              NOT NULL,
    name      VARCHAR          NOT NULL,
    latitude  DOUBLE PRECISION NOT NULL,
    longitude DOUBLE PRECISION NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT unique_location UNIQUE (user_id, latitude, longitude)
);