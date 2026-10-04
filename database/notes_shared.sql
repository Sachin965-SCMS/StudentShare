CREATE DATABASE IF NOT EXISTS notes_shared CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE notes_shared;

CREATE TABLE colleges (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(150) NOT NULL,
    email VARCHAR(190) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uq_college_name (name), UNIQUE KEY uq_college_email (email)
) ENGINE=InnoDB;

CREATE TABLE courses (
    id INT PRIMARY KEY AUTO_INCREMENT,
    college_id INT NOT NULL,
    code VARCHAR(30) NOT NULL,
    name VARCHAR(120) NOT NULL,
    semester_count INT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_courses_college FOREIGN KEY (college_id) REFERENCES colleges(id) ON DELETE CASCADE,
    UNIQUE KEY uq_course_code_college (college_id, code), UNIQUE KEY uq_course_name_college (college_id, name)
) ENGINE=InnoDB;

CREATE TABLE semesters (
    id INT PRIMARY KEY AUTO_INCREMENT,
    course_id INT NOT NULL,
    semester_number INT NOT NULL,
    CONSTRAINT fk_semesters_course FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE,
    UNIQUE KEY uq_semester_course (course_id, semester_number)
) ENGINE=InnoDB;

CREATE TABLE subjects (
    id INT PRIMARY KEY AUTO_INCREMENT,
    semester_id INT NOT NULL,
    name VARCHAR(150) NOT NULL,
    CONSTRAINT fk_subjects_semester FOREIGN KEY (semester_id) REFERENCES semesters(id) ON DELETE CASCADE,
    UNIQUE KEY uq_subject_name_semester (semester_id, name)
) ENGINE=InnoDB;

CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    college_id INT NOT NULL,
    course_id INT NULL,
    name VARCHAR(120) NOT NULL,
    email VARCHAR(190) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role ENUM('ADMIN','SUPERVISOR','STUDENT') NOT NULL,
    approval_status ENUM('APPROVED','PENDING','REJECTED') NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_users_college FOREIGN KEY (college_id) REFERENCES colleges(id) ON DELETE CASCADE,
    CONSTRAINT fk_users_course FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE SET NULL,
    UNIQUE KEY uq_user_email (email),
    UNIQUE KEY uq_supervisor_per_course (course_id, role),
    KEY idx_users_pending (college_id, approval_status, role), KEY idx_users_course (course_id)
) ENGINE=InnoDB;

CREATE TABLE materials (
    id INT PRIMARY KEY AUTO_INCREMENT,
    college_id INT NOT NULL,
    course_id INT NOT NULL,
    uploaded_by INT NOT NULL,
    title VARCHAR(180) NOT NULL,
    description VARCHAR(500) NULL,
    material_type ENUM('PDF','PPT','QUESTION_PAPER','NOTES') NOT NULL,
    original_filename VARCHAR(255) NOT NULL,
    stored_filename VARCHAR(255) NOT NULL,
    content_type VARCHAR(120) NOT NULL,
    file_size BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_material_college FOREIGN KEY (college_id) REFERENCES colleges(id) ON DELETE CASCADE,
    CONSTRAINT fk_material_course FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE,
    CONSTRAINT fk_material_uploader FOREIGN KEY (uploaded_by) REFERENCES users(id) ON DELETE CASCADE,
    KEY idx_material_course_created (college_id, course_id, created_at), KEY idx_material_type (college_id, material_type)
) ENGINE=InnoDB;

CREATE TABLE ratings (
    id INT PRIMARY KEY AUTO_INCREMENT,
    material_id INT NOT NULL,
    student_id INT NOT NULL,
    rating TINYINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_rating_material FOREIGN KEY (material_id) REFERENCES materials(id) ON DELETE CASCADE,
    CONSTRAINT fk_rating_student FOREIGN KEY (student_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT ck_rating_range CHECK (rating BETWEEN 1 AND 5),
    UNIQUE KEY uq_student_material_rating (material_id, student_id)
) ENGINE=InnoDB;
