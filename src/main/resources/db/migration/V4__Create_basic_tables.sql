CREATE TABLE users(
    id    UUID PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL
);


CREATE TABLE user_profiles(
    id      UUID PRIMARY KEY,
    bio     TEXT,

    user_id UUID UNIQUE REFERENCES users (id) ON DELETE CASCADE
);


CREATE TABLE roles(
    id   UUID PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);


ALTER TABLE users
    ADD COLUMN role_id UUID REFERENCES roles (id) ON DELETE SET NULL;