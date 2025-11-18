package com.sams.controller;

import com.sams.entity.Course;
import com.sams.entity.Student;
import com.sams.service.CourseService;
import com.sams.util.CourseCache;
import com.sams.service.StudentService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class ManageStudentsController {
    @FXML
    private TableView<Student> studentsTable;
    @FXML
    private TableColumn<Student, Integer> idColumn;
    @FXML
    private TableColumn<Student, String> nameColumn;
    @FXML
    private TableColumn<Student, String> regNumberColumn;
    @FXML
    private TableColumn<Student, String> courseColumn;
    @FXML
    private TableColumn<Student, String> contactColumn;
    @FXML
    private TextField nameField;
    @FXML
    private TextField regNumberField;
    @FXML
    private ComboBox<Course> courseComboBox;
    @FXML
    private TextField contactField;

    private StudentService studentService = new StudentService();
    private ObservableList<Student> studentList = FXCollections.observableArrayList();
    private Student selectedStudent;

    @FXML
    private void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        regNumberColumn.setCellValueFactory(new PropertyValueFactory<>("registrationNumber"));
        courseColumn.setCellValueFactory(
                cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getCourse().getName()));
        contactColumn.setCellValueFactory(new PropertyValueFactory<>("contact"));

        // Bind the combo box to the shared course list so updates are visible
        // everywhere
        courseComboBox.setItems(CourseCache.getCourses());

        loadStudents();

        studentsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedStudent = newSelection;
                nameField.setText(selectedStudent.getName());
                regNumberField.setText(selectedStudent.getRegistrationNumber());
                courseComboBox.setValue(selectedStudent.getCourse());
                contactField.setText(selectedStudent.getContact());
            }
        });
    }

    private void loadStudents() {
        studentList.clear();
        studentList.addAll(studentService.getAllStudents());
        studentsTable.setItems(studentList);
    }

    @FXML
    private void addStudent() {
        Student student = new Student();
        student.setName(nameField.getText());
        student.setRegistrationNumber(regNumberField.getText());
        student.setCourse(courseComboBox.getValue());
        student.setContact(contactField.getText());
        try {
            studentService.saveStudent(student);
            loadStudents();
            showInfo("Student added successfully");
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void updateStudent() {
        if (selectedStudent != null) {
            selectedStudent.setName(nameField.getText());
            selectedStudent.setRegistrationNumber(regNumberField.getText());
            selectedStudent.setCourse(courseComboBox.getValue());
            selectedStudent.setContact(contactField.getText());
            studentService.updateStudent(selectedStudent);
            loadStudents();
            showInfo("Student updated successfully");
        } else {
            showError("Select a student to update");
        }
    }

    @FXML
    private void deleteStudent() {
        if (selectedStudent != null) {
            studentService.deleteStudent(selectedStudent.getId());
            loadStudents();
            showInfo("Student deleted successfully");
        } else {
            showError("Select a student to delete");
        }
    }

    @FXML
    private void clearForm() {
        nameField.clear();
        regNumberField.clear();
        courseComboBox.setValue(null);
        contactField.clear();
        selectedStudent = null;
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