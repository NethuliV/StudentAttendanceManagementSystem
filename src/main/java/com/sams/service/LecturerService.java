package com.sams.service;

import com.sams.dao.LecturerDAO;
import com.sams.entity.Lecturer;

import java.util.List;

public class LecturerService {
    private LecturerDAO lecturerDAO = new LecturerDAO();

    public void saveLecturer(Lecturer lecturer) {
        if (lecturer.getName() == null || lecturer.getName().isEmpty()) {
            throw new IllegalArgumentException("Lecturer name cannot be empty");
        }
        lecturerDAO.save(lecturer);
    }

    public Lecturer getLecturerById(int id) {
        return lecturerDAO.findById(id);
    }

    public void updateLecturer(Lecturer lecturer) {
        lecturerDAO.update(lecturer);
    }

    public void deleteLecturer(int id) {
        lecturerDAO.delete(id);
    }

    public List<Lecturer> getAllLecturers() {
        return lecturerDAO.findAll();
    }
}