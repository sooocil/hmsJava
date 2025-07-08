# Hospital Management System

Hey! This is my **Hospital Management System**, a Java desktop app to manage hospital tasks like patient records and rooms. Built with Java Swing and MySQL, it’s a project I’m excited about to show my coding skills. It’s still growing, but here’s what it’s got so far!

## Features

*   **Login**: Secure username/password check via MySQL.
*   **Admin Panel**: Clean sidebar with tabs for:
    *   **Dashboard**: Stats like patient count (placeholder).
    *   **Add New Patient**: Form to save name, age, gender to database.
    *   **All Patient Info**: Table for patient data (WIP).
    *   **Discharge Patient**: Form to update patient status.
    *   **Add Room**: Form for room details.
    *   Other tabs (Department, Employee Info, etc.) in progress.
*   **UI**: Modern navy sidebar, light blue active tabs, green-bordered title.
*   **Database**: MySQL for data storage/retrieval.

## Setup

1.  **Clone**:
    
        git clone https://github.com/your-username/hospital-management-system.git
        
    
2.  **Requirements**:
    
    *   Java 8+ (JDK 17 works great).
    *   MySQL 8.0 + MySQL Connector/J (add to classpath).
    *   IDE (IntelliJ, Eclipse, etc.).
3.  **Database**:
    
        CREATE DATABASE hospital_db;
        USE hospital_db;
        CREATE TABLE users (username VARCHAR(50) PRIMARY KEY, password VARCHAR(50));
        CREATE TABLE patients (id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(100), age INT, gender VARCHAR(20));
        CREATE TABLE rooms (room_number VARCHAR(10) PRIMARY KEY, type VARCHAR(20));
        INSERT INTO users (username, password) VALUES ('admin', 'admin123');
        
    
4.  **Run**:
    
    *   Update `conn.java` with MySQL credentials.
    *   Run `Login.java`, use `admin`/`admin123` to log in.

## Future Plans

*   Finish tabs with tables and forms.
*   Add input validation.
*   Improve UI with icons.

## Screenshots

_Will add images of the app soon!_

## Contact

Got feedback? Hit me up on GitHub or file an issue. Thanks for checking it out!
