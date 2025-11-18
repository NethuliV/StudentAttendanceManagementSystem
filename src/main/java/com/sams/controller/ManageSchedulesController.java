package com.sams.controller;

import com.sams.entity.ClassSchedule;
import com.sams.entity.Course;
import com.sams.entity.Lecturer;
import com.sams.service.ClassScheduleService;
import com.sams.service.CourseService;
import com.sams.util.CourseCache;
import com.sams.service.LecturerService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class ManageSchedulesController {
    @FXML
    private TableView<ClassSchedule> schedulesTable;
    @FXML
    private TableColumn<ClassSchedule, Integer> idColumn;
    @FXML
    private TableColumn<ClassSchedule, String> courseColumn;
    @FXML
    private TableColumn<ClassSchedule, String> subjectColumn;
    @FXML
    private TableColumn<ClassSchedule, Date> dateColumn;
    @FXML
    private TableColumn<ClassSchedule, String> lecturerColumn;
    @FXML
    private ComboBox<Course> courseComboBox;
    @FXML
    private TextField subjectField;
    @FXML
    private DatePicker datePicker;
    @FXML
    private ComboBox<Lecturer> lecturerComboBox;

    private ClassScheduleService scheduleService = new ClassScheduleService();
    private LecturerService lecturerService = new LecturerService();
    private ObservableList<ClassSchedule> scheduleList = FXCollections.observableArrayList();
    private ObservableList<Lecturer> lecturerOptions = FXCollections.observableArrayList();
    private ClassSchedule selectedSchedule;

    @FXML
    private void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        courseColumn.setCellValueFactory(
                cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getCourse().getName()));
        subjectColumn.setCellValueFactory(new PropertyValueFactory<>("subject"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        lecturerColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getLecturer().getName()));

        // Bind to shared course list so UI updates when courses change
        courseComboBox.setItems(CourseCache.getCourses());

        lecturerOptions.addAll(lecturerService.getAllLecturers());
        lecturerComboBox.setItems(lecturerOptions);

        loadSchedules();

        schedulesTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedSchedule = newSelection;
                courseComboBox.setValue(selectedSchedule.getCourse());
                subjectField.setText(selectedSchedule.getSubject());
                datePicker
                        .setValue(selectedSchedule.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                lecturerComboBox.setValue(selectedSchedule.getLecturer());
            }
        });
    }

    private void loadSchedules() {
        scheduleList.clear();
        scheduleList.addAll(scheduleService.getAllClassSchedules());
        schedulesTable.setItems(scheduleList);
    }

    @FXML
    private void addSchedule() {
        ClassSchedule schedule = new ClassSchedule();
        schedule.setCourse(courseComboBox.getValue());
        schedule.setSubject(subjectField.getText());
        LocalDate localDate = datePicker.getValue();
        if (localDate != null) {
            schedule.setDate(Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        }
        schedule.setLecturer(lecturerComboBox.getValue());
        try {
            scheduleService.saveClassSchedule(schedule);
            loadSchedules();
            showInfo("Schedule added successfully");
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void updateSchedule() {
        if (selectedSchedule != null) {
            selectedSchedule.setCourse(courseComboBox.getValue());
            selectedSchedule.setSubject(subjectField.getText());
            LocalDate localDate = datePicker.getValue();
            if (localDate != null) {
                selectedSchedule.setDate(Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
            }
            selectedSchedule.setLecturer(lecturerComboBox.getValue());
            scheduleService.updateClassSchedule(selectedSchedule);
            loadSchedules();
            showInfo("Schedule updated successfully");
        } else {
            showError("Select a schedule to update");
        }
    }

    @FXML
    private void deleteSchedule() {
        if (selectedSchedule != null) {
            scheduleService.deleteClassSchedule(selectedSchedule.getId());
            loadSchedules();
            showInfo("Schedule deleted successfully");
        } else {
            showError("Select a schedule to delete");
        }
    }

    @FXML
    private void clearForm() {
        courseComboBox.setValue(null);
        subjectField.clear();
        datePicker.setValue(null);
        lecturerComboBox.setValue(null);
        selectedSchedule = null;
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