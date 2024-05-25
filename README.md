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
## logo 
![Capture d'écran 2024-05-04 210948](https://github.com/RayenMarzouk6/Student-Registration-Java-App-/assets/162569033/937a58f7-ebf3-4be3-a975-d8aa38bd1b61)

## Screenshots

![Capture d'écran 2024-05-24 202605](https://github.com/RayenMarzouk6/Student-Registration-Java-App-/assets/162569033/d85536d5-66c5-4dfc-991f-a19d07cf68fa)

## Usage

1. **Login**: Upon launching the application, users are prompted to enter their credentials (username and password) to access the system.![Capture d'écran 2024-05-24 201609](https://github.com/RayenMarzouk6/Student-Registration-Java-App-/assets/162569033/4e0f7ca7-d5dd-4b9f-8de7-bb22ffea1bbe)

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
