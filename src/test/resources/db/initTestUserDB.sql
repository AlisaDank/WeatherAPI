create schema test_db;

CREATE TABLE users
(
    id SERIAL PRIMARY KEY,
    login VARCHAR(50) UNIQUE,
    password VARCHAR(100) NOT NULL
);