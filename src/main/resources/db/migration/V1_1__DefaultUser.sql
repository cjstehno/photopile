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

/*
  Bootstrap in the admin user at installation (username: admin, password: admin)
  The password data is the encoded version using the BCryptPasswordEncoder - if a different encoder is used
  this will need to change.
*/

INSERT INTO users (version, username, password, enabled) VALUES (
  1,
  'admin',
  '$2a$10$a.6OATQO9WgdkhTWgJWqxuiTGPdmlWteJxF4SkSRRKNyieIPXZ2Yu',
  TRUE
);

INSERT INTO authorities (authority) VALUES ('ADMIN');

INSERT INTO user_authorities (user_id, authority_id) VALUES (1, 1);