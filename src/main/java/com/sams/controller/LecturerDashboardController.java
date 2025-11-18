package com.sams.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import java.io.PrintWriter;
import java.io.StringWriter;
import com.sams.util.CurrentUser;

import java.io.IOException;

public class LecturerDashboardController {
    @FXML
    private void openManageSchedules() {
        openNewStage("/manage_schedules.fxml", "Manage Schedules");
    }

    @FXML
    private void openMarkAttendance() {
        openNewStage("/mark_attendance.fxml", "Mark Attendance");
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
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            TextArea ta = new TextArea(sw.toString());
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