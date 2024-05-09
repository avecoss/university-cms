CREATE SCHEMA IF NOT EXISTS university;

CREATE TABLE IF NOT EXISTS university.group
(
    group_id   SERIAL PRIMARY KEY,
    group_name VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS university.student
(
    student_id BIGSERIAL PRIMARY KEY,
    group_id   INT          REFERENCES university.group (group_id) ON DELETE SET NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name  VARCHAR(100) NOT NULL,
    username   VARCHAR(100) NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    u_role     VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS university.teacher
(
    teacher_id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name  VARCHAR(100) NOT NULL,
    username   VARCHAR(100) NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    u_role     VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS university.course
(
    course_id   SERIAL PRIMARY KEY,
    teacher_id  INT          REFERENCES university.teacher (teacher_id) ON DELETE SET NULL,
    course_name VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS university.student_course
(
    student_id BIGINT REFERENCES university.student (student_id) ON DELETE CASCADE,
    course_id  INT REFERENCES university.course (course_id) ON DELETE CASCADE,
    PRIMARY KEY (student_id, course_id)
);