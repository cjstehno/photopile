
create table images (
  photo_id bigint not null references photos (id),
  scale varchar(10) not null,
  width int not null,
  height int not null,
  content_length bigint not null,
  content_type varchar(50) not null,
  content oid not null,
  unique (photo_id,scale)
);

