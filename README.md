# Student Attendance Management System (SAMS)

## Overview

A system to manage student attendance, courses, lecturers, schedules, and reports for educational institutions.

## Setup Instructions

1. Install JDK 17, Maven, MySQL.
2. Create database `sams_db` in MySQL and run `schema.sql` to set up tables and sample data.
3. Update `hibernate.cfg.xml` with your MySQL credentials.
4. Run `mvn clean install` to build.
5. Run `mvn javafx:run` to start the app.

## Technologies Used

- JavaFX for UI
- Hibernate for database connectivity
- MySQL for database
- Maven for build management

## Login Credentials

- Admin: username `admin`, password `adminpass`
- Lecturer: username `lecturer1`, password `lectpass`


