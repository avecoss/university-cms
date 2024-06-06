INSERT INTO university.group (group_name)
VALUES ('AA-01'),
       ('AA-02'),
       ('BB-01'),
       ('BB-02'),
       ('CC-01');

INSERT INTO university.user (first_name, last_name, username, password, email)
VALUES
    ('John', 'Doe', 'johdoe', '$2a$10$liRaeDItocgYR6N6WIkR8elO4PwokkREulbMWgN7S1nFX/vBXDI2G', 'johdoe@eample.com'), -- {bcrypt} password
    ('Jane', 'Smith', 'jansmi', '$2a$10$liRaeDItocgYR6N6WIkR8elO4PwokkREulbMWgN7S1nFX/vBXDI2G', 'jansmi@eample.com'),
    ('Alex', 'Johnson', 'alejoh', '$2a$10$liRaeDItocgYR6N6WIkR8elO4PwokkREulbMWgN7S1nFX/vBXDI2G', 'alejoh@eample.com'),
    ('Samantha', 'Smith', 'samsmi', '$2a$10$liRaeDItocgYR6N6WIkR8elO4PwokkREulbMWgN7S1nFX/vBXDI2G', 'samsmi@eample.com'),
    ('David', 'Brown', 'davbro', '$2a$10$liRaeDItocgYR6N6WIkR8elO4PwokkREulbMWgN7S1nFX/vBXDI2G', 'davbro@eample.com'),
    ('Emily', 'Wilson', 'emiwils', '$2a$10$liRaeDItocgYR6N6WIkR8elO4PwokkREulbMWgN7S1nFX/vBXDI2G', 'emiwils@eample.com'),
    ('Jacob', 'Martinez', 'jacmar', '$2a$10$liRaeDItocgYR6N6WIkR8elO4PwokkREulbMWgN7S1nFX/vBXDI2G', 'jacmar@eample.com'),
    ('Jacob', 'Smith', 'jacsmi', '$2a$10$liRaeDItocgYR6N6WIkR8elO4PwokkREulbMWgN7S1nFX/vBXDI2G', 'jacsmi@eample.com'),
    ('Olivia', 'Taylor', 'olitay', '$2a$10$liRaeDItocgYR6N6WIkR8elO4PwokkREulbMWgN7S1nFX/vBXDI2G', 'olitay@eample.com'),
    ('Ethan', 'Anderson', 'ethand', '$2a$10$liRaeDItocgYR6N6WIkR8elO4PwokkREulbMWgN7S1nFX/vBXDI2G', 'ethand@eample.com'),
    ('Sophia', 'Garcia', 'sopgar', '$2a$10$liRaeDItocgYR6N6WIkR8elO4PwokkREulbMWgN7S1nFX/vBXDI2G', 'sopgar@eample.com'),
    ('Michael', 'Johnson', 'micjoh', '$2a$10$liRaeDItocgYR6N6WIkR8elO4PwokkREulbMWgN7S1nFX/vBXDI2G', 'micjoh@eample.com'),
    ('Alex', 'Williams', 'alewil', '$2a$10$liRaeDItocgYR6N6WIkR8elO4PwokkREulbMWgN7S1nFX/vBXDI2G', 'alewil@eample.com'),
    ('Emily', 'Williams', 'emiwill', '$2a$10$liRaeDItocgYR6N6WIkR8elO4PwokkREulbMWgN7S1nFX/vBXDI2G', 'emiwill@eample.com'),
    ('David', 'Anderson', 'davand', '$2a$10$liRaeDItocgYR6N6WIkR8elO4PwokkREulbMWgN7S1nFX/vBXDI2G', 'davand@eample.com'),
    ('John', 'Brown', 'johbro', '$2a$10$liRaeDItocgYR6N6WIkR8elO4PwokkREulbMWgN7S1nFX/vBXDI2G', 'johbro@eample.com'),
    ('Sam', 'Gray', 'samgra', '$2a$10$liRaeDItocgYR6N6WIkR8elO4PwokkREulbMWgN7S1nFX/vBXDI2G', 'samgra@eample.com');

INSERT INTO university.student (user_id, group_id)
VALUES
    (1, 1), -- John Doe
    (2, 2), -- Jane Smith
    (3, 2), -- Alex Johnson
    (4, 1), -- Samantha Smith
    (5, 1), -- David Brown
    (6, 3), -- Emily Wilson
    (7, 1), -- Jacob Martinez
    (8, 4), -- Jacob Smith
    (9, 5), -- Olivia Taylor
    (10, 5), -- Ethan Anderson
    (11, 3); -- Sophia Garcia

INSERT INTO university.teacher (user_id)
VALUES
    (12), -- Michael Johnson
    (13), -- Alex Williams
    (14), -- Emily Williams
    (15), -- David Anderson
    (16), -- John Brown
    (17); -- Sam Gray

INSERT INTO university.user_authority (user_id, authority_id)
VALUES
    (1, 1), -- John Doe -> STUDENT
    (2, 1), -- Jane Smith -> STUDENT
    (3, 1), -- Alex Johnson -> STUDENT
    (4, 1), -- Samantha Smith -> STUDENT
    (5, 1), -- David Brown -> STUDENT
    (6, 1), -- Emily Wilson -> STUDENT
    (7, 1), -- Jacob Martinez -> STUDENT
    (8, 1), -- Jacob Smith -> STUDENT
    (9, 1), -- Olivia Taylor -> STUDENT
    (10, 1), -- Ethan Anderson -> STUDENT
    (11, 1), -- Sophia Garcia -> STUDENT
    (12, 2), -- Michael Johnson -> TEACHER
    (13, 2), -- Alex Williams -> TEACHER
    (14, 2), -- Emily Williams -> TEACHER
    (15, 2), -- David Anderson -> TEACHER
    (16, 2), -- John Brown -> TEACHER
    (17, 2); -- Sam Gray -> TEACHER

INSERT INTO university.course (teacher_id, course_name)
VALUES
    (1, 'Mathematics'),
    (1, 'Physics'),
    (2, 'Chemistry'),
    (3, 'History'),
    (4, 'Literature'),
    (4, 'Foreign Language'),
    (3, 'Art'),
    (5, 'Economics'),
    (3, 'Geography');

INSERT INTO university.student_course (student_id, course_id)
VALUES
    (1, 1),  -- John Doe -> Mathematics
    (1, 2),  -- John Doe -> Physics
    (2, 1),  -- Jane Smith -> Mathematics
    (3, 1),  -- Alex Johnson -> Mathematics
    (4, 2),  -- Samantha Smith -> Physics
    (5, 2),  -- David Brown -> Physics
    (6, 3),  -- Emily Wilson -> Chemistry
    (7, 4),  -- Jacob Martinez -> History
    (8, 5),  -- Jacob Smith -> Literature
    (9, 6), -- Olivia Taylor -> Foreign Language
    (10, 7), -- Ethan Anderson -> Art
    (11, 8), -- Sophia Garcia -> Economics
    (11, 7), -- Sophia Garcia -> Art
    (11, 9), -- Sophia Garcia -> Geography
    (10, 9); -- Ethan Anderson -> Geography