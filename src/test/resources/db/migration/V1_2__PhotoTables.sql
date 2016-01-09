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

CREATE TABLE photos (
  id            SERIAL      NOT NULL PRIMARY KEY,
  version       BIGINT    DEFAULT 0,
  name          VARCHAR(50) NOT NULL,
  description   VARCHAR(2000),
  hash          VARCHAR(64),

  date_uploaded TIMESTAMP DEFAULT now(),
  date_updated  TIMESTAMP DEFAULT now(),
  date_taken    TIMESTAMP,

  geo_latitude  DOUBLE PRECISION,
  geo_longitude DOUBLE PRECISION,
  geo_altitude  INT
);

CREATE TABLE tags (
  id       SERIAL      NOT NULL PRIMARY KEY,
  category VARCHAR(20) NOT NULL,
  label    VARCHAR(40) NOT NULL,
  UNIQUE (category, label)
);

CREATE TABLE photo_tags (
  photo_id BIGINT NOT NULL REFERENCES photos (id),
  tag_id   BIGINT NOT NULL REFERENCES tags (id),
  PRIMARY KEY (photo_id, tag_id)
);

CREATE TABLE images (
  id             SERIAL      NOT NULL PRIMARY KEY,
  scale          VARCHAR(10) NOT NULL,
  width          INT         NOT NULL,
  height         INT         NOT NULL,
  content_length BIGINT      NOT NULL,
  content_type   VARCHAR(50) NOT NULL
);

CREATE TABLE photo_images (
  photo_id BIGINT NOT NULL REFERENCES photos (id),
  image_id BIGINT NOT NULL REFERENCES images (id),
  PRIMARY KEY (photo_id, image_id)
);

CREATE TABLE albums (
  id           SERIAL      NOT NULL PRIMARY KEY,
  version      BIGINT    DEFAULT 0,
  name         VARCHAR(50) NOT NULL,
  description  VARCHAR(2000),
  date_created TIMESTAMP DEFAULT now(),
  date_updated TIMESTAMP DEFAULT now()
);

CREATE TABLE album_photos (
  album_id BIGINT NOT NULL REFERENCES albums (id),
  photo_id BIGINT NOT NULL REFERENCES photos (id),
  PRIMARY KEY (album_id, photo_id)
);