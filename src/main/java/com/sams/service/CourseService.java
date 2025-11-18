package com.sams.service;

import com.sams.dao.CourseDAO;
import com.sams.entity.Course;

import java.util.List;

public class CourseService {
    private CourseDAO courseDAO = new CourseDAO();

    public void saveCourse(Course course) {
        // Add business logic if needed, e.g., validate name not empty
        if (course.getName() == null || course.getName().isEmpty()) {
            throw new IllegalArgumentException("Course name cannot be empty");
        }
        courseDAO.save(course);
    }

    public Course getCourseById(int id) {
        return courseDAO.findById(id);
    }

    public void updateCourse(Course course) {
        courseDAO.update(course);
    }

    public void deleteCourse(int id) {
        courseDAO.delete(id);
    }

    public List<Course> getAllCourses() {
        return courseDAO.findAll();
    }
}