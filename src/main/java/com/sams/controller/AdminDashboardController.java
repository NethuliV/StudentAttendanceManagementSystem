package com.sams.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.sams.util.CurrentUser;

import java.io.IOException;

public class AdminDashboardController {
    @FXML
    private void openManageCourses() {
        openNewStage("/manage_courses.fxml", "Manage Courses");
    }

    @FXML
    private void openManageStudents() {
        openNewStage("/manage_students.fxml", "Manage Students");
    }

    @FXML
    private void openManageLecturers() {
        openNewStage("/manage_lecturers.fxml", "Manage Lecturers");
    }

    @FXML
    private void openManageSchedules() {
        openNewStage("/manage_schedules.fxml", "Manage Schedules");
    }

    @FXML
    private void openViewReports() {
        openNewStage("/view_reports.fxml", "View Reports");
    }

    @FXML
    private void logout() {
        CurrentUser.clear();
        openNewStage("/login.fxml", "Login");
    }

    private void openNewStage(String fxmlPath, String title) {
        try {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            // Show detailed error dialog with stack trace to help debugging
            java.io.StringWriter sw = new java.io.StringWriter();
            e.printStackTrace(new java.io.PrintWriter(sw));
            javafx.scene.control.TextArea ta = new javafx.scene.control.TextArea(sw.toString());
            ta.setEditable(false);
            ta.setWrapText(true);
            ta.setMaxWidth(Double.MAX_VALUE);
            ta.setMaxHeight(Double.MAX_VALUE);
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                    javafx.scene.control.Alert.AlertType.ERROR);
            alert.setHeaderText("Error opening window: " + e.toString());
            alert.setContentText("An error occurred while opening the window. Expand for details.");
            alert.getDialogPane().setExpandableContent(ta);
            alert.showAndWait();
        }
    }

    private void showError(String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.showAndWait();
    }
}