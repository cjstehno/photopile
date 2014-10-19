
alter table photos add column geo_altitude int;
alter table photos rename column latitude to geo_latitude;
alter table photos rename column longitude to geo_longitude;

alter table photos drop column camera_info;
alter table photos add column camera_make varchar(20);
alter table photos add column camera_model varchar(20);
