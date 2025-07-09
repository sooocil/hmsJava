

create database hospital_management_system;
use hospital_management_system;

create table login(ID varchar(20), PW varchar(20));
select * from login;


insert into login value("admin", "admin123");

-- Patient Information Table Creation   
CREATE TABLE patient_info (
    patient_id INT PRIMARY KEY auto_increment,
    name VARCHAR(255),
    gender VARCHAR(50),
    age VARCHAR(10),
    contact VARCHAR(20),
    citizen_no VARCHAR(50),
    room_no INT,
    address VARCHAR(255),
    disease_condition VARCHAR(255),
    deposit VARCHAR(50),
    time_date VARCHAR(50)
);

INSERT INTO patient_info VALUES  ('Sushil Regmi','Male', '30','9800000000','123456789', 101,         'Kathmandu','Flu',           '2000',  NOW());

select * from patient_info;

-- Delete/Drop Patient Information Table From db
drop table patient_info;

DESCRIBE patient_info;

select * from patient_info;


-- Room Information Table
create table room(room_no varchar(20), availability boolean, price varchar(20), room_type varchar(100));
insert into room value(101, true, 1200, 'ICU');
select * from room;
drop table room;


-- Discharge table
CREATE TABLE discharge_info (
    id             INT AUTO_INCREMENT PRIMARY KEY,   
    patient_id     INT NOT NULL,                    
    discharge_date DATETIME NOT NULL DEFAULT NOW(), 
    payment_done   BOOLEAN NOT NULL DEFAULT FALSE,   

    -- using patient
    CONSTRAINT fk_discharge_patient
        FOREIGN KEY (patient_id)
        REFERENCES patient_info (patient_id)
        ON DELETE CASCADE
);

select * from discharge_info;

-- Drop discharge_info Table From db
drop table discharge_info;



-- Employee Information Table
CREATE TABLE employee_info (
    employee_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255),
    position VARCHAR(100),
    department VARCHAR(100),
    contact VARCHAR(20),
    salary DECIMAL(10,2)
);


-- Ambulance Information Table
create table Ambulance 


