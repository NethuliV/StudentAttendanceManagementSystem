-- Users table (for login: Admin and Lecturer roles)
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL, -- Store hashed passwords in real apps
    role ENUM('ADMIN', 'LECTURER') NOT NULL
);

-- Courses table
CREATE TABLE courses (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    subject VARCHAR(100) NOT NULL
);

-- Students table
CREATE TABLE students (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    registration_number VARCHAR(50) UNIQUE NOT NULL,
    course_id INT,
    contact VARCHAR(100),
    FOREIGN KEY (course_id) REFERENCES courses(id)
);

-- Lecturers table
CREATE TABLE lecturers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    subject VARCHAR(100) NOT NULL
);

-- Class Schedules table
CREATE TABLE class_schedules (
    id INT AUTO_INCREMENT PRIMARY KEY,
    course_id INT NOT NULL,
    subject VARCHAR(100) NOT NULL,
    date DATE NOT NULL,
    lecturer_id INT NOT NULL,
    FOREIGN KEY (course_id) REFERENCES courses(id),
    FOREIGN KEY (lecturer_id) REFERENCES lecturers(id)
);

-- Attendance table
CREATE TABLE attendances (
    id INT AUTO_INCREMENT PRIMARY KEY,
    class_schedule_id INT NOT NULL,
    student_id INT NOT NULL,
    present BOOLEAN NOT NULL DEFAULT FALSE,
    FOREIGN KEY (class_schedule_id) REFERENCES class_schedules(id),
    FOREIGN KEY (student_id) REFERENCES students(id)
);

-- Sample data
INSERT INTO users (username, password, role) VALUES ('admin', 'adminpass', 'ADMIN');
INSERT INTO users (username, password, role) VALUES ('lecturer1', 'lectpass', 'LECTURER');

INSERT INTO courses (name, subject) VALUES ('Intro to Programming', 'Java');

INSERT INTO students (name, registration_number, course_id, contact) VALUES ('John Doe', 'REG001', 1, 'john@example.com');

INSERT INTO lecturers (name, subject) VALUES ('Dr. Smith', 'Java');