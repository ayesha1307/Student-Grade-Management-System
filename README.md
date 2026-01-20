# Student Grade Management System

A Java application for managing student grades, attendance, and academic records using SQLite database.

## Features

- Student Management
  - Add, view, update, and delete student records
  - Store student personal information

- Subject Management
  - Add, view, update, and delete subjects
  - Track credit hours for each subject

- Grade Management
  - Record grades for students in different subjects
  - Calculate student GPA
  - View grade history

- Attendance Management
  - Mark student attendance
  - View attendance records
  - Calculate attendance percentage

- Report Generation
  - Generate student performance reports
  - Generate attendance summary reports

## Requirements

- Java JDK 8 or higher
- SQLite JDBC Driver

## Setup

1. Clone the repository
2. Add the SQLite JDBC driver to your project's classpath
3. Compile all Java files:
   ```
   javac *.java
   ```
4. Run the application:
   ```
   java StudentGradeManagement
   ```

## Database Schema

The application uses the following tables:

- `students`: Stores student information
- `subjects`: Stores subject information
- `grades`: Stores student grades
- `attendance`: Stores attendance records

## Usage

1. Start the application
2. Use the main menu to navigate through different management options
3. Follow the on-screen prompts to perform various operations

## Contributing

Feel free to submit issues and enhancement requests.

## License

This project is open source and available under the MIT License. 