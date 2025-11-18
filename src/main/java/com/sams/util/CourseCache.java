package com.sams.util;

import com.sams.entity.Course;
import com.sams.service.CourseService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Simple shared cache for courses so UI controllers can bind to a single
 * ObservableList and update automatically when courses are refreshed.
 */
public class CourseCache {
    private static final ObservableList<Course> courses = FXCollections.observableArrayList();
    private static final CourseService courseService = new CourseService();

    public static ObservableList<Course> getCourses() {
        if (courses.isEmpty()) {
            refresh();
        }
        return courses;
    }

    public static void refresh() {
        courses.clear();
        courses.addAll(courseService.getAllCourses());
    }
}
