package com.sams.controller;

import com.sams.entity.Attendance;
import com.sams.entity.ClassSchedule;
import com.sams.entity.Student;
import com.sams.service.AttendanceService;
import com.sams.service.ClassScheduleService;
import com.sams.service.StudentService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;

public class MarkAttendanceController {
    @FXML
    private ComboBox<ClassSchedule> scheduleComboBox;
    @FXML
    private TableView<AttendanceWrapper> attendanceTable;
    @FXML
    private TableColumn<AttendanceWrapper, Integer> idColumn;
    @FXML
    private TableColumn<AttendanceWrapper, String> nameColumn;
    @FXML
    private TableColumn<AttendanceWrapper, String> regNumberColumn;
    @FXML
    private TableColumn<AttendanceWrapper, Boolean> presentColumn;

    private ClassScheduleService scheduleService = new ClassScheduleService();
    private StudentService studentService = new StudentService();
    private AttendanceService attendanceService = new AttendanceService();
    private ObservableList<ClassSchedule> scheduleOptions = FXCollections.observableArrayList();
    private ObservableList<AttendanceWrapper> attendanceWrappers = FXCollections.observableArrayList();

    // Wrapper class for checkbox
    public static class AttendanceWrapper {
        private Student student;
        private boolean present;

        public AttendanceWrapper(Student student, boolean present) {
            this.student = student;
            this.present = present;
        }

        public Integer getId() {
            return student.getId();
        }

        public String getName() {
            return student.getName();
        }

        public String getRegNumber() {
            return student.getRegistrationNumber();
        }

        public boolean isPresent() {
            return present;
        }

        public void setPresent(boolean present) {
            this.present = present;
        }

        public Student getStudent() {
            return student;
        }
    }

    @FXML
    private void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        regNumberColumn.setCellValueFactory(new PropertyValueFactory<>("regNumber"));
        presentColumn.setCellValueFactory(new PropertyValueFactory<>("present"));
        presentColumn.setCellFactory(CheckBoxTableCell.forTableColumn(presentColumn));
        presentColumn.setEditable(true);
        attendanceTable.setEditable(true);

        scheduleOptions.addAll(scheduleService.getAllClassSchedules());
        scheduleComboBox.setItems(scheduleOptions);
    }

    @FXML
    private void loadStudents() {
        ClassSchedule selectedSchedule = scheduleComboBox.getValue();
        if (selectedSchedule != null) {
            attendanceWrappers.clear();
            // Assume students are from the course
            List<Student> students = studentService.getAllStudents().stream()
                    .filter(s -> s.getCourse().getId() == selectedSchedule.getCourse().getId())
                    .collect(Collectors.toList());

            // Load existing attendances or default to false
            List<Attendance> existing = attendanceService.getAttendanceByClassSchedule(selectedSchedule.getId());
            Map<Integer, Boolean> attendanceMap = existing.stream()
                    .collect(Collectors.toMap(a -> a.getStudent().getId(), Attendance::isPresent));

            for (Student student : students) {
                boolean isPresent = attendanceMap.getOrDefault(student.getId(), false);
                attendanceWrappers.add(new AttendanceWrapper(student, isPresent));
            }
            attendanceTable.setItems(attendanceWrappers);
        } else {
            showError("Select a schedule first");
        }
    }

    @FXML
    private void saveAttendance() {
        ClassSchedule selectedSchedule = scheduleComboBox.getValue();
        if (selectedSchedule != null) {
            for (AttendanceWrapper wrapper : attendanceWrappers) {
                Attendance attendance = new Attendance();
                attendance.setClassSchedule(selectedSchedule);
                attendance.setStudent(wrapper.getStudent());
                attendance.setPresent(wrapper.isPresent());
                // Check if exists, update or save
                // For simplicity, assume save overwrites if unique constraint on
                // schedule+student
                attendanceService.saveAttendance(attendance);
            }
            showInfo("Attendance saved successfully");
        } else {
            showError("Select a schedule first");
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