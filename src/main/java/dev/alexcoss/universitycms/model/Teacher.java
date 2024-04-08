package dev.alexcoss.universitycms.model;

import java.util.Set;

public class Teacher extends Person {

    private long id;
    private Set<Course> courses;
}
