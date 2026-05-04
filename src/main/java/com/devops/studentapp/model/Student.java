package com.devops.studentapp.model;

public class Student {
    private int id;
    private String name;
    private String course;
    private double cgpa;

    public Student() {}

    public Student(int id, String name, String course, double cgpa) {
        this.id = id;
        this.name = name;
        this.course = course;
        this.cgpa = cgpa;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }

    public double getCgpa() { return cgpa; }
    public void setCgpa(double cgpa) { this.cgpa = cgpa; }
}
