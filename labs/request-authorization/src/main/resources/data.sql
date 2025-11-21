INSERT INTO users (username, password, enabled) VALUES ('r2d2', 'legend1', '1');
INSERT INTO users (username, password, enabled) VALUES ('c3po', 'legend2', '1');
INSERT INTO users (username, password, enabled) VALUES ('bb8', 'legend3', '1');

INSERT INTO authorities (username, authority) VALUES ('r2d2', 'READ');
INSERT INTO authorities (username, authority) VALUES ('r2d2', 'WRITE');
INSERT INTO authorities (username, authority) VALUES ('c3po', 'WRITE');
INSERT INTO authorities (username, authority) VALUES ('c3po', 'DELETE');

INSERT INTO authorities (username, authority) VALUES ('r2d2', 'ROLE_ADMIN');
INSERT INTO authorities (username, authority) VALUES ('r2d2', 'ROLE_USER');
INSERT INTO authorities (username, authority) VALUES ('c3po', 'ROLE_USER');
INSERT INTO authorities (username, authority) VALUES ('c3po', 'ROLE_MANAGER');
