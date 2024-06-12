INSERT INTO university.authority (name)
VALUES ('STUFF');

INSERT INTO university.user_authority (user_id, authority_id)
VALUES ((SELECT user_id FROM university.user WHERE username = 'johdoe'),
        (SELECT authority_id FROM university.authority WHERE name = 'STUFF'));