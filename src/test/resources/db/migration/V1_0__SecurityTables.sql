--
-- Copyright (C) 2016 Christopher J. Stehno <chris@stehno.com>
--
-- Licensed under the Apache License, Version 2.0 (the "License");
-- you may not use this file except in compliance with the License.
-- You may obtain a copy of the License at
--
--         http://www.apache.org/licenses/LICENSE-2.0
--
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.
--

-- Based on http://docs.spring.io/spring-security/site/docs/3.2.4.RELEASE/reference/htmlsingle/#user-schema

CREATE TABLE users (
  id                  SERIAL       NOT NULL PRIMARY KEY,
  version             BIGINT  DEFAULT 0,
  username            VARCHAR(25)  NOT NULL UNIQUE,
  display_name        VARCHAR(30),
  password            VARCHAR(100) NOT NULL,
  enabled             BOOLEAN      NOT NULL,
  account_expired     BOOLEAN DEFAULT FALSE,
  credentials_expired BOOLEAN DEFAULT FALSE,
  account_locked      BOOLEAN DEFAULT FALSE
);

CREATE TABLE authorities (
  id        SERIAL      NOT NULL PRIMARY KEY,
  authority VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE user_authorities (
  user_id      BIGINT NOT NULL REFERENCES users (id),
  authority_id BIGINT NOT NULL REFERENCES authorities (id),
  PRIMARY KEY (user_id, authority_id)
);