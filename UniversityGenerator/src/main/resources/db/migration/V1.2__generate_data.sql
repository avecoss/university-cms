INSERT INTO university.group (group_name)
VALUES ('AA-01'),
       ('AA-02'),
       ('BB-01'),
       ('BB-02'),
       ('CC-01');

INSERT INTO university.student (group_id, first_name, last_name, username, password, u_role)
VALUES (1, 'John', 'Doe', 'johdoe', 'password', 'STUDENT'),
       (2, 'Jane', 'Smith', 'jansmi', 'password', 'STUDENT'),
       (2, 'Alex', 'Johnson', 'alejoh', 'password', 'STUDENT'),
       (1, 'Samantha', 'Smith', 'samsmi', 'password', 'STUDENT'),
       (1, 'David', 'Brown', 'davbro', 'password', 'STUDENT'),
       (3, 'Emily', 'Wilson', 'emiwil', 'password', 'STUDENT'),
       (1, 'Jacob', 'Martinez', 'jacmar', 'password', 'STUDENT'),
       (4, 'Jacob', 'Smith', 'jacsmi', 'password', 'STUDENT'),
       (5, 'Olivia', 'Taylor', 'olitay', 'password', 'STUDENT'),
       (5, 'Ethan', 'Anderson', 'ethand', 'password', 'STUDENT'),
       (3, 'Sophia', 'Garcia', 'sopgar', 'password', 'STUDENT');


INSERT INTO university.teacher (first_name, last_name, username, password, u_role)
VALUES ('Michael', 'Johnson', 'micjoh', 'password', 'TEACHER'),
       ('Alex', 'Williams', 'alewil', 'password', 'TEACHER'),
       ('Emily', 'Williams', 'emiwil', 'password', 'TEACHER'),
       ('David', 'Anderson', 'davand', 'password', 'TEACHER'),
       ('John', 'Brown', 'johbro', 'password', 'TEACHER'),
       ('Admin', 'Admin', 'admin', 'password', 'ADMIN'),
       ('Sam', 'Gray', 'samgra', 'password', 'TEACHER');

INSERT INTO university.course (teacher_id, course_name)
VALUES (1, 'Mathematics'),
       (1, 'Physics'),
       (2, 'Chemistry'),
       (3, 'History'),
       (4, 'Literature'),
       (4, 'Foreign Language'),
       (3, 'Art'),
       (5, 'Economics'),
       (3, 'Geography');

INSERT INTO university.student_course (student_id, course_id)
VALUES (1, 1),  -- John Doe -> Mathematics
       (1, 2),  -- John Doe -> Physics
       (2, 1),  -- Jane Smith -> Mathematics
       (3, 1),  -- Alex Johnson -> Mathematics
       (4, 2),  -- Samantha Smith -> Physics
       (5, 2),  -- David Brown -> Physics
       (6, 3),  -- Emily Wilson -> Chemistry
       (7, 4),  -- Jacob Martinez -> History
       (8, 5),  -- Jacob Smith -> Literature
       (9, 6),  -- Olivia Taylor -> Foreign Language
       (10, 7), -- Ethan Anderson -> Art
       (11, 8), -- Sophia Garcia -> Economics
       (11, 7), -- Sophia Garcia -> Art
       (11, 9), -- Sophia Garcia -> Geography
       (10, 9); -- Jacob Smith -> Geography