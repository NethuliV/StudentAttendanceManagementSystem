package com.sams.controller;

import com.sams.entity.User;
import com.sams.service.UserService;
import com.sams.util.CurrentUser;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    private UserService userService = new UserService();

    @FXML
    private void login() {
        try {
            String username = usernameField.getText();
            String password = passwordField.getText();

            if (username == null || username.isBlank() || password == null || password.isBlank()) {
                showError("Please enter username and password");
                return;
            }

            User user = userService.login(username, password);
            if (user != null) {
                CurrentUser.setUser(user);
                // Inform the user (debug) which role was detected so we can verify redirection
                // logic
                showInfo("Login successful: " + user.getUsername() + " (role=" + user.getRole() + ")");
                try {
                    Stage stage = (Stage) usernameField.getScene().getWindow();
                    Parent root;
                    if ("ADMIN".equals(user.getRole())) {
                        root = FXMLLoader.load(getClass().getResource("/admin_dashboard.fxml"));
                    } else {
                        root = FXMLLoader.load(getClass().getResource("/lecturer_dashboard.fxml"));
                    }
                    stage.setScene(new Scene(root));
                } catch (IOException e) {
                    e.printStackTrace();
                    showError("Error loading dashboard: " + e.getMessage());
                }
            } else {
                showError("Invalid credentials");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            showError("Login failed: " + ex.getClass().getSimpleName() + " - " + ex.getMessage());
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Small helper to show informational messages (used for debugging login flow)
    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }
}