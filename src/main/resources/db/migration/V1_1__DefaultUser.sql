/*
  Bootstrap in the admin user at installation (username: admin, password: admin)

  The password data is the encoded version using the BCryptPasswordEncoder - if a different encoder is used
  this will need to change.
*/

INSERT INTO users (id, version, username, password, enabled) VALUES (
  1,
  1,
  'admin',
  '$2a$10$a.6OATQO9WgdkhTWgJWqxuiTGPdmlWteJxF4SkSRRKNyieIPXZ2Yu',
  TRUE
);

INSERT INTO authorities (id, authority) VALUES (2, 'ADMIN');

INSERT INTO user_authorities (user_id, authority_id) VALUES (1, 2);
