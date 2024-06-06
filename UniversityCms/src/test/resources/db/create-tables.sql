CREATE SCHEMA IF NOT EXISTS university;

CREATE TABLE IF NOT EXISTS university.user
(
    user_id    BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name  VARCHAR(100) NOT NULL,
    username   VARCHAR(255) NOT NULL UNIQUE CHECK ( length(trim(username)) > 2),
    password   VARCHAR(255) NOT NULL,
    email      VARCHAR(255) NOT NULL UNIQUE
    );

CREATE TABLE IF NOT EXISTS university.group
(
    group_id   SERIAL PRIMARY KEY,
    group_name VARCHAR(100) NOT NULL
    );

CREATE TABLE IF NOT EXISTS university.student
(
    student_id BIGSERIAL PRIMARY KEY,
    user_id    BIGINT REFERENCES university.user (user_id) ON DELETE CASCADE,
    group_id   INT REFERENCES university.group (group_id) ON DELETE SET NULL
    );

CREATE TABLE IF NOT EXISTS university.teacher
(
    teacher_id BIGSERIAL PRIMARY KEY,
    user_id    BIGINT REFERENCES university.user (user_id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS university.course
(
    course_id   SERIAL PRIMARY KEY,
    teacher_id  INT          REFERENCES university.teacher (teacher_id) ON DELETE SET NULL,
    course_name VARCHAR(100) NOT NULL
    );

CREATE TABLE IF NOT EXISTS university.authority
(
    authority_id SERIAL PRIMARY KEY,
    name         VARCHAR(100) NOT NULL
    );

CREATE TABLE IF NOT EXISTS university.student_course
(
    student_id BIGINT REFERENCES university.student (student_id) ON DELETE CASCADE,
    course_id  INT REFERENCES university.course (course_id) ON DELETE CASCADE,
    PRIMARY KEY (student_id, course_id)
    );

CREATE TABLE IF NOT EXISTS university.user_authority
(
    user_id      BIGINT REFERENCES university.user (user_id) ON DELETE CASCADE,
    authority_id INT REFERENCES university.authority (authority_id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, authority_id)
    );