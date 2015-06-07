-- rim of database
CREATE TABLE owner (
  id   INTEGER          NOT NULL PRIMARY KEY AUTOINCREMENT,
  name VARCHAR(255) NOT NULL
);

CREATE TABLE device (
  id       INTEGER          NOT NULL PRIMARY KEY AUTOINCREMENT,
  name     VARCHAR(255) NOT NULL,
  owner_id INTEGER          NOT NULL,
  FOREIGN KEY (owner_id) REFERENCES owner (id)
  ON UPDATE CASCADE
  ON DELETE CASCADE
);

CREATE TABLE key (
  id          INTEGER          NOT NULL PRIMARY KEY AUTOINCREMENT,
  license_key VARCHAR(255) NOT NULL,
  in_use      BOOLEAN      NOT NULL DEFAULT 0,
  device_id   INTEGER DEFAULT NULL,
  FOREIGN KEY (device_id) REFERENCES device (id)
  ON UPDATE CASCADE
  ON DELETE SET DEFAULT
);

CREATE UNIQUE INDEX owner_name ON owner (name);
CREATE UNIQUE INDEX device_name ON device (name);
CREATE UNIQUE INDEX license_key_id ON key (license_key);
