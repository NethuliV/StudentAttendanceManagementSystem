package com.sams.service;

import com.sams.dao.StudentDAO;
import com.sams.entity.Student;

import java.util.List;

public class StudentService {
    private StudentDAO studentDAO = new StudentDAO();

    public void saveStudent(Student student) {
        // Validate registration number unique (DAO can handle, but check here too)
        if (student.getRegistrationNumber() == null || student.getRegistrationNumber().isEmpty()) {
            throw new IllegalArgumentException("Registration number cannot be empty");
        }
        studentDAO.save(student);
    }

    public Student getStudentById(int id) {
        return studentDAO.findById(id);
    }

    public Student getStudentByRegistrationNumber(String regNum) {
        return studentDAO.findByRegistrationNumber(regNum);
    }

    public void updateStudent(Student student) {
        studentDAO.update(student);
    }

    public void deleteStudent(int id) {
        studentDAO.delete(id);
    }

    public List<Student> getAllStudents() {
        return studentDAO.findAll();
    }
}