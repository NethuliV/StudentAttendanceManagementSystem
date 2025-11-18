package com.sams.service;

import com.sams.dao.ClassScheduleDAO;
import com.sams.entity.ClassSchedule;

import java.util.Date;
import java.util.List;

public class ClassScheduleService {
    private ClassScheduleDAO classScheduleDAO = new ClassScheduleDAO();

    public void saveClassSchedule(ClassSchedule classSchedule) {
        // Check if schedule already exists for course and date
        ClassSchedule existing = classScheduleDAO.findByCourseAndDate(classSchedule.getCourse().getId(),
                classSchedule.getDate());
        if (existing != null) {
            throw new IllegalArgumentException("Schedule already exists for this course and date");
        }
        classScheduleDAO.save(classSchedule);
    }

    public ClassSchedule getClassScheduleById(int id) {
        return classScheduleDAO.findById(id);
    }

    public ClassSchedule getClassScheduleByCourseAndDate(int courseId, Date date) {
        return classScheduleDAO.findByCourseAndDate(courseId, date);
    }

    public void updateClassSchedule(ClassSchedule classSchedule) {
        classScheduleDAO.update(classSchedule);
    }

    public void deleteClassSchedule(int id) {
        classScheduleDAO.delete(id);
    }

    public List<ClassSchedule> getAllClassSchedules() {
        return classScheduleDAO.findAll();
    }
}