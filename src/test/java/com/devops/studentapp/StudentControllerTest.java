package com.devops.studentapp;

import com.devops.studentapp.model.Student;
import com.devops.studentapp.service.StudentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class StudentControllerTest {

    private StudentService studentService;

    @BeforeEach
    void setUp() {
        studentService = new StudentService();
    }

    @Test
    void testGetAllStudents() {
        List<Student> students = studentService.getAllStudents();
        assertNotNull(students);
        assertFalse(students.isEmpty());
        assertTrue(students.size() >= 3);
    }

    @Test
    void testGetStudentById_Found() {
        Optional<Student> student = studentService.getStudentById(1);
        assertTrue(student.isPresent());
        assertEquals("Arjun Sharma", student.get().getName());
    }

    @Test
    void testGetStudentById_NotFound() {
        Optional<Student> student = studentService.getStudentById(999);
        assertFalse(student.isPresent());
    }

    @Test
    void testAddStudent() {
        Student newStudent = new Student(0, "Test Student", "B.Tech CSE", 8.0);
        Student added = studentService.addStudent(newStudent);
        assertNotNull(added);
        assertEquals("Test Student", added.getName());
        assertTrue(added.getId() > 0);
    }

    @Test
    void testStudentFields() {
        Student s = new Student(1, "Arjun Sharma", "B.Tech CSE", 8.9);
        assertEquals(1, s.getId());
        assertEquals("Arjun Sharma", s.getName());
        assertEquals("B.Tech CSE", s.getCourse());
        assertEquals(8.9, s.getCgpa());
    }
}