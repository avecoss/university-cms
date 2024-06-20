CREATE TABLE IF NOT EXISTS university.schedule
(
    schedule_id BIGSERIAL PRIMARY KEY,
    group_id    INT       REFERENCES university.group (group_id) ON DELETE SET NULL,
    course_id   INT       REFERENCES university.course (course_id) ON DELETE SET NULL,
    start_time  TIMESTAMP NOT NULL,
    end_time    TIMESTAMP NOT NULL
);