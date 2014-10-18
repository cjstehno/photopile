
-- Based on http://docs.spring.io/spring-security/site/docs/3.2.4.RELEASE/reference/htmlsingle/#user-schema

create table users(
  userid serial not null primary key,
  username varchar(25) not null,
  password varchar(100) not null,
  enabled boolean not null,
  account_expired boolean not null default false,
  credentials_expired boolean not null default false,
  account_locked boolean not null default false
);

create table authorities (
  userid bigint not null,
  authority varchar(50) not null,
  constraint fk_authorities_users foreign key(userid) references users(userid)
);
create unique index ix_auth_username on authorities (userid,authority);


/*
  Bootstrap in the admin user at installation (username: admin, password: admin)

  The password data is the encoded version using the BCryptPasswordEncoder - if a different encoder is used
  this will need to change.
*/

insert into users (userid,username,password,enabled) values (1,'admin','$2a$10$a.6OATQO9WgdkhTWgJWqxuiTGPdmlWteJxF4SkSRRKNyieIPXZ2Yu',true);

insert into authorities (userid, authority) values (1, 'ADMIN');
