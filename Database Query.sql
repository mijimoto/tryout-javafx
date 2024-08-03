CREATE DATABASE WaterManagementSystem
------
USE WaterManagementSystem

-- Create Users table
CREATE TABLE Users (
    id INT PRIMARY KEY,
    accountName VARCHAR(255),
    password VARCHAR(255),
    name VARCHAR(255),
    age INT,
    address VARCHAR(255),
    phoneNumber VARCHAR(255),
    email VARCHAR(255)
);

-- Create UserMeter table
CREATE TABLE UserMeter (
    Meterid INT,
    id INT,
    PRIMARY KEY (Meterid, id),
    FOREIGN KEY (id) REFERENCES Users(id)
);

-- Create WaterMeter table
CREATE TABLE WaterMeter (
    Meterid INT PRIMARY KEY,
    WaterMeter VARCHAR(255),
    MeterName VARCHAR(255),
    MeterMonth INT,
    MeterDate DATE,
    MeterYear INT,
    FOREIGN KEY (Meterid) REFERENCES UserMeter(Meterid)
);
