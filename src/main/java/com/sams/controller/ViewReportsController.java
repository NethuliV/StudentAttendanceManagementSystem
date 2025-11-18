package com.sams.controller;

import com.sams.entity.Attendance;
import com.sams.entity.Course;
import com.sams.entity.Student;
import com.sams.service.AttendanceService;
import com.sams.service.CourseService;
import com.sams.util.CourseCache;
import com.sams.service.StudentService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ViewReportsController {
    @FXML
    private ComboBox<Student> studentComboBox;
    @FXML
    private ComboBox<Course> courseComboBox;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private TableView<Attendance> reportTable;
    @FXML
    private TableColumn<Attendance, Date> dateColumn;
    @FXML
    private TableColumn<Attendance, String> courseColumn;
    @FXML
    private TableColumn<Attendance, String> studentColumn;
    @FXML
    private TableColumn<Attendance, Boolean> presentColumn;

    private AttendanceService attendanceService = new AttendanceService();
    private StudentService studentService = new StudentService();
    private CourseService courseService = new CourseService();
    private ObservableList<Student> studentOptions = FXCollections.observableArrayList();
    private ObservableList<Attendance> reportList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        dateColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(
                cellData.getValue().getClassSchedule().getDate()));
        courseColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getClassSchedule().getCourse().getName()));
        studentColumn.setCellValueFactory(
                cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getStudent().getName()));
        presentColumn.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().isPresent()));
        presentColumn.setCellFactory(CheckBoxTableCell.forTableColumn(presentColumn));

        studentOptions.addAll(studentService.getAllStudents());
        studentComboBox.setItems(studentOptions);

        // Use shared course cache so new courses appear in the dropdown automatically
        courseComboBox.setItems(CourseCache.getCourses());
    }

    @FXML
    private void generateReport() {
        Student selectedStudent = studentComboBox.getValue();
        // Course filter not directly in service, but for simplicity, assume filter
        // post-query
        LocalDate startLocal = startDatePicker.getValue();
        LocalDate endLocal = endDatePicker.getValue();
        if (selectedStudent != null && startLocal != null && endLocal != null) {
            Date startDate = Date.from(startLocal.atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date endDate = Date.from(endLocal.atStartOfDay(ZoneId.systemDefault()).toInstant());
            List<Attendance> attendances = attendanceService.getReportByStudentAndDateRange(selectedStudent.getId(),
                    startDate, endDate);
            // Optional: Filter by course if selected
            Course selectedCourse = courseComboBox.getValue();
            if (selectedCourse != null) {
                attendances = attendances.stream()
                        .filter(a -> a.getClassSchedule().getCourse().getId() == selectedCourse.getId())
                        .collect(Collectors.toList());
            }
            reportList.clear();
            reportList.addAll(attendances);
            reportTable.setItems(reportList);
        } else {
            showError("Select student and date range");
        }
    }

    @FXML
    private void exportToCSV() {
        if (reportList.isEmpty()) {
            showError("Generate a report first");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Report");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showSaveDialog(new Stage());

        if (file != null) {
            try (FileWriter writer = new FileWriter(file)) {
                writer.write("Date,Course,Student,Present\n");
                for (Attendance att : reportList) {
                    writer.write(att.getClassSchedule().getDate() + "," +
                            att.getClassSchedule().getCourse().getName() + "," +
                            att.getStudent().getName() + "," +
                            att.isPresent() + "\n");
                }
                showInfo("Report exported successfully");
            } catch (IOException e) {
                showError("Error exporting: " + e.getMessage());
            }
        }
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