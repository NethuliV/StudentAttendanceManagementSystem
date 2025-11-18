package com.sams.service;

import com.sams.dao.UserDAO;
import com.sams.entity.User;

import java.util.List;

public class UserService {
    private UserDAO userDAO = new UserDAO();

    public User login(String username, String password) {
        User user = userDAO.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) { // In real apps, hash passwords
            return user;
        }
        return null;
    }

    public void saveUser(User user) {
        userDAO.save(user);
    }

    // Add updateUser, deleteUser, getAllUsers
    public void updateUser(User user) {
        userDAO.update(user);
    }

    public void deleteUser(int id) {
        userDAO.delete(id);
    }

    public List<User> getAllUsers() {
        return userDAO.findAll();
    }
}