import java.sql.*;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:student_grade_management.db";
    private Connection connection;

    public void initializeDatabase() throws SQLException {
        connection = DriverManager.getConnection(DB_URL);
        createTables();
    }

    private void createTables() throws SQLException {
        // Create Students table
        String createStudentsTable = "CREATE TABLE IF NOT EXISTS students (" +
                "student_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "first_name TEXT NOT NULL," +
                "last_name TEXT NOT NULL," +
                "date_of_birth TEXT," +
                "email TEXT," +
                "phone TEXT)";

        // Create Subjects table
        String createSubjectsTable = "CREATE TABLE IF NOT EXISTS subjects (" +
                "subject_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "subject_name TEXT NOT NULL," +
                "credit_hours INTEGER NOT NULL)";

        // Create Grades table
        String createGradesTable = "CREATE TABLE IF NOT EXISTS grades (" +
                "grade_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "student_id INTEGER," +
                "subject_id INTEGER," +
                "grade_value REAL," +
                "semester TEXT," +
                "FOREIGN KEY (student_id) REFERENCES students(student_id)," +
                "FOREIGN KEY (subject_id) REFERENCES subjects(subject_id))";

        // Create Attendance table
        String createAttendanceTable = "CREATE TABLE IF NOT EXISTS attendance (" +
                "attendance_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "student_id INTEGER," +
                "subject_id INTEGER," +
                "date TEXT," +
                "status TEXT," +
                "FOREIGN KEY (student_id) REFERENCES students(student_id)," +
                "FOREIGN KEY (subject_id) REFERENCES subjects(subject_id))";

        try (Statement statement = connection.createStatement()) {
            statement.execute(createStudentsTable);
            statement.execute(createSubjectsTable);
            statement.execute(createGradesTable);
            statement.execute(createAttendanceTable);
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println("Error closing database connection: " + e.getMessage());
        }
    }
} 