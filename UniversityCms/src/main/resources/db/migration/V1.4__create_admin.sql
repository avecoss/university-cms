INSERT INTO university.user (first_name, last_name, username, password, email)
VALUES ('Admin', 'Admin', 'admin', '$2a$10$liRaeDItocgYR6N6WIkR8elO4PwokkREulbMWgN7S1nFX/vBXDI2G','admin@universitycms.com'); -- {bcrypt} password

INSERT INTO university.user_authority (user_id, authority_id)
SELECT user_id, 3 FROM university.user WHERE username = 'admin'; -- Admin -> ADMIN