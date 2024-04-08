package dev.alexcoss.universitycms.model;

import java.util.Set;

public class Student extends Person {

    private long id;
    private Group group;
    private Set<Course> courses;
}
