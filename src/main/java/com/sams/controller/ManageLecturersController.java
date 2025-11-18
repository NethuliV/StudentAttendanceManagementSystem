package com.sams.controller;

import com.sams.entity.Lecturer;
import com.sams.service.LecturerService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class ManageLecturersController {
    @FXML
    private TableView<Lecturer> lecturersTable;
    @FXML
    private TableColumn<Lecturer, Integer> idColumn;
    @FXML
    private TableColumn<Lecturer, String> nameColumn;
    @FXML
    private TableColumn<Lecturer, String> subjectColumn;
    @FXML
    private TextField nameField;
    @FXML
    private TextField subjectField;

    private LecturerService lecturerService = new LecturerService();
    private ObservableList<Lecturer> lecturerList = FXCollections.observableArrayList();
    private Lecturer selectedLecturer;

    @FXML
    private void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        subjectColumn.setCellValueFactory(new PropertyValueFactory<>("subject"));
        loadLecturers();

        lecturersTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedLecturer = newSelection;
                nameField.setText(selectedLecturer.getName());
                subjectField.setText(selectedLecturer.getSubject());
            }
        });
    }

    private void loadLecturers() {
        lecturerList.clear();
        lecturerList.addAll(lecturerService.getAllLecturers());
        lecturersTable.setItems(lecturerList);
    }

    @FXML
    private void addLecturer() {
        Lecturer lecturer = new Lecturer();
        lecturer.setName(nameField.getText());
        lecturer.setSubject(subjectField.getText());
        try {
            lecturerService.saveLecturer(lecturer);
            loadLecturers();
            showInfo("Lecturer added successfully");
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void updateLecturer() {
        if (selectedLecturer != null) {
            selectedLecturer.setName(nameField.getText());
            selectedLecturer.setSubject(subjectField.getText());
            lecturerService.updateLecturer(selectedLecturer);
            loadLecturers();
            showInfo("Lecturer updated successfully");
        } else {
            showError("Select a lecturer to update");
        }
    }

    @FXML
    private void deleteLecturer() {
        if (selectedLecturer != null) {
            lecturerService.deleteLecturer(selectedLecturer.getId());
            loadLecturers();
            showInfo("Lecturer deleted successfully");
        } else {
            showError("Select a lecturer to delete");
        }
    }

    @FXML
    private void clearForm() {
        nameField.clear();
        subjectField.clear();
        selectedLecturer = null;
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