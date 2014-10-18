
create table photos (
  id serial not null primary key,
  version bigint not null default 0,
  name varchar(50) not null,
  description varchar(2000),
  camera_info varchar(100),
  date_uploaded timestamp not null default now(),
  date_updated timestamp not null default now(),
  date_taken timestamp,
  latitude double precision,
  longitude double precision
);

create table tags (
  id serial not null primary key,
  name varchar(40) not null unique
);

create table photo_tags (
  photo_id bigint not null references photos (id),
  tag_id bigint not null references tags (id),
  unique( photo_id, tag_id )
);
