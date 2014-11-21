CREATE TABLE photos (
  id            SERIAL      NOT NULL PRIMARY KEY,
  version       BIGINT      NOT NULL DEFAULT 1,
  name          VARCHAR(50) NOT NULL,
  description   VARCHAR(2000),

  camera_make   VARCHAR(25),
  camera_model  VARCHAR(25),

  date_uploaded TIMESTAMP   NOT NULL DEFAULT now(),
  date_updated  TIMESTAMP   NOT NULL DEFAULT now(),
  date_taken    TIMESTAMP,

  geo_latitude  DOUBLE PRECISION,
  geo_longitude DOUBLE PRECISION,
  geo_altitude  INT
);

CREATE TABLE tags (
  id       SERIAL      NOT NULL PRIMARY KEY,
  version  BIGINT      NOT NULL DEFAULT 1,
  category VARCHAR(20) NOT NULL,
  name     VARCHAR(40) NOT NULL,
  UNIQUE (category, name)
);

CREATE TABLE photo_tags (
  photo_id BIGINT NOT NULL REFERENCES photos (id),
  tag_id   BIGINT NOT NULL REFERENCES tags (id),
  PRIMARY KEY (photo_id, tag_id)
);

CREATE TABLE images (
  photo_id       BIGINT      NOT NULL REFERENCES photos (id),
  version        BIGINT      NOT NULL DEFAULT 1,
  scale          VARCHAR(10) NOT NULL,
  width          INT         NOT NULL,
  height         INT         NOT NULL,
  content_length BIGINT      NOT NULL,
  content_type   VARCHAR(50) NOT NULL,
  PRIMARY KEY (photo_id, scale)
);
