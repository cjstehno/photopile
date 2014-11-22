CREATE SEQUENCE hibernate_sequence START 3;

-- Based on http://docs.spring.io/spring-security/site/docs/3.2.4.RELEASE/reference/htmlsingle/#user-schema

CREATE TABLE users (
  id      BIGINT NOT NULL PRIMARY KEY,
  version BIGINT DEFAULT 0,
  username            VARCHAR(25)  NOT NULL UNIQUE,
  password            VARCHAR(100) NOT NULL,
  enabled             BOOLEAN      NOT NULL,
  account_expired     BOOLEAN DEFAULT FALSE,
  credentials_expired BOOLEAN DEFAULT FALSE,
  account_locked      BOOLEAN DEFAULT FALSE
);

CREATE TABLE authorities (
  id        BIGINT      NOT NULL PRIMARY KEY,
  authority VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE user_authorities (
  user_id BIGINT NOT NULL REFERENCES users (id),
  authority_id BIGINT NOT NULL REFERENCES authorities (id),
  PRIMARY KEY (user_id, authority_id)
);

