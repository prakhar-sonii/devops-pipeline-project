package com.devops.studentapp.service;

import com.devops.studentapp.model.Student;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private final List<Student> students = new ArrayList<>();

    public StudentService() {
        // Pre-loaded sample data
        students.add(new Student(1, "Arjun Sharma", "B.Tech CSE", 8.9));
        students.add(new Student(2, "Priya Mehta", "B.Tech IT", 9.2));
        students.add(new Student(3, "Rohit Singh", "B.Tech ECE", 7.8));
    }

    public List<Student> getAllStudents() {
        return students;
    }

    public Optional<Student> getStudentById(int id) {
        return students.stream()
                .filter(s -> s.getId() == id)
                .findFirst();
    }

    public Student addStudent(Student student) {
        student.setId(students.size() + 1);
        students.add(student);
        return student;
    }
}
