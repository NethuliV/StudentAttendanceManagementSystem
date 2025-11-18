package com.sams.service;

import com.sams.dao.AttendanceDAO;
import com.sams.entity.Attendance;

import java.util.Date;
import java.util.List;

public class AttendanceService {
    private AttendanceDAO attendanceDAO = new AttendanceDAO();

    public void saveAttendance(Attendance attendance) {
        // Business logic: Ensure attendance not already marked for this student/class
        // You can add a unique constraint in DB, but check here optionally
        attendanceDAO.save(attendance);
    }

    public Attendance getAttendanceById(int id) {
        return attendanceDAO.findById(id);
    }

    public List<Attendance> getAttendanceByClassSchedule(int scheduleId) {
        return attendanceDAO.findByClassSchedule(scheduleId);
    }

    public List<Attendance> getReportByStudentAndDateRange(int studentId, Date startDate, Date endDate) {
        // Business logic: Calculate percentage or format report if needed
        return attendanceDAO.findByStudentAndDateRange(studentId, startDate, endDate);
    }

    public void updateAttendance(Attendance attendance) {
        attendanceDAO.update(attendance);
    }

    public void deleteAttendance(int id) {
        attendanceDAO.delete(id);
    }

    public List<Attendance> getAllAttendances() {
        return attendanceDAO.findAll();
    }
}