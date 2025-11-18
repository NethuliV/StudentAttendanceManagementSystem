package com.sams.controller;

import com.sams.entity.Course;
import com.sams.service.CourseService;
import com.sams.util.CourseCache;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class ManageCoursesController {
    @FXML
    private TableView<Course> coursesTable;
    @FXML
    private TableColumn<Course, Integer> idColumn;
    @FXML
    private TableColumn<Course, String> nameColumn;
    @FXML
    private TableColumn<Course, String> subjectColumn;
    @FXML
    private TextField courseNameField;
    @FXML
    private TextField subjectField;

    private CourseService courseService = new CourseService();
    private ObservableList<Course> courseList = FXCollections.observableArrayList();
    private Course selectedCourse;

    @FXML
    private void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        subjectColumn.setCellValueFactory(new PropertyValueFactory<>("subject"));
        // Ensure shared course cache is populated for other controllers
        CourseCache.refresh();
        loadCourses();

        coursesTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedCourse = newSelection;
                courseNameField.setText(selectedCourse.getName());
                subjectField.setText(selectedCourse.getSubject());
            }
        });
    }

    private void loadCourses() {
        courseList.clear();
        courseList.addAll(courseService.getAllCourses());
        coursesTable.setItems(courseList);
    }

    @FXML
    private void addCourse() {
        Course course = new Course();
        course.setName(courseNameField.getText());
        course.setSubject(subjectField.getText());
        try {
            courseService.saveCourse(course);
            // refresh shared cache so other windows see the new course
            CourseCache.refresh();
            loadCourses();
            showInfo("Course added successfully");
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void updateCourse() {
        if (selectedCourse != null) {
            selectedCourse.setName(courseNameField.getText());
            selectedCourse.setSubject(subjectField.getText());
            courseService.updateCourse(selectedCourse);
            CourseCache.refresh();
            loadCourses();
            showInfo("Course updated successfully");
        } else {
            showError("Select a course to update");
        }
    }

    @FXML
    private void deleteCourse() {
        if (selectedCourse != null) {
            courseService.deleteCourse(selectedCourse.getId());
            CourseCache.refresh();
            loadCourses();
            showInfo("Course deleted successfully");
        } else {
            showError("Select a course to delete");
        }
    }

    @FXML
    private void clearForm() {
        courseNameField.clear();
        subjectField.clear();
        selectedCourse = null;
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.showAndWait();
    }
}