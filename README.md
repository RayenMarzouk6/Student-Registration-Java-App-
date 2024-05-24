# Student Registration App

Welcome to the Student Registration App! This Java application allows administrators to register, manage, and search for student records. Administrators can add new students, delete existing ones, and search for students by their ID. The application also supports adding photos for each student.

## Database Setup

To set up the database for this application, follow these steps:

1. **Create the database**:
   - Ensure that MySQL is installed and running on your local machine.
   - Execute the following SQL command to create a database named `students_db`:
     ```sql
     CREATE DATABASE students_db;
     ```

2. **Create the table**:
   - After creating the database, execute the following SQL command to create a table named `students`:
     ```sql
     CREATE TABLE students (
         code_eleve INT AUTO_INCREMENT PRIMARY KEY,
         nom VARCHAR(255),
         sexe VARCHAR(10),
         classe VARCHAR(10),
         photo LONGBLOB
     );
     ```

## Screenshots

![Screenshot 1](https://example.com/screenshot1.png)
![Screenshot 2](https://example.com/screenshot2.png)
![Screenshot 3](https://example.com/screenshot3.png)

## Usage

1. **Login**: Upon launching the application, users are prompted to enter their credentials (username and password) to access the system.
2. **Registration**: Administrators can register new students by entering their details and selecting an image for their photo.
3. **Deletion**: Administrators can delete students by selecting them from the table and clicking the "Delete" button.
4. **Search**: Users can search for students by their ID using the search field provided.

## Dependencies

- Java Swing for the GUI
- MySQL for the database

## Build and Run

1. Clone this repository to your local machine.
2. Open the project in your preferred Java IDE.
3. Configure the MySQL connection settings in the code.
4. Run the application.
