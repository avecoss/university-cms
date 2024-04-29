INSERT INTO university.group (group_name)
SELECT 'Group ' || g
FROM generate_series(1, 10) AS g;

INSERT INTO university.teacher (first_name, last_name, username, password)
SELECT 'Teacher' || t, 'LastName' || t, 'teacher' || t, 'password' || t
FROM generate_series(1, 10) AS t;

INSERT INTO university.course (teacher_id, course_name)
SELECT t.teacher_id, 'Course ' || c
FROM generate_series(1, 10) AS c
         CROSS JOIN university.teacher t
ORDER BY random() LIMIT 20;

INSERT INTO university.student (group_id, first_name, last_name, username, password)
SELECT g.group_id,
       'Student' || g.group_id || s,
       'LastName' || s,
       'student' || g.group_id || s,
       'password' || s
FROM university.group g
         CROSS JOIN generate_series(1, 5) AS s;


INSERT INTO student_course (student_id, course_id)
SELECT s.student_id,
       c.course_id
FROM university.student s
         CROSS JOIN university.course c
ORDER BY random() LIMIT 250;