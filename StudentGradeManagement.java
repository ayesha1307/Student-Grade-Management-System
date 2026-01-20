import java.sql.*;
import java.util.Scanner;

public class StudentGradeManagement {
    private static final Scanner scanner = new Scanner(System.in);
    private static final DatabaseManager dbManager = new DatabaseManager();
    private static Student studentManager;
    private static Grade gradeManager;
    private static Attendance attendanceManager;
    private static Subject subjectManager;

    public static void main(String[] args) {
        try {
            dbManager.initializeDatabase();
            studentManager = new Student(dbManager.getConnection());
            gradeManager = new Grade(dbManager.getConnection());
            attendanceManager = new Attendance(dbManager.getConnection());
            subjectManager = new Subject(dbManager.getConnection());
            showMainMenu();
        } catch (SQLException e) {
            System.out.println("Error initializing database: " + e.getMessage());
        } finally {
            dbManager.closeConnection();
        }
    }

    private static void showMainMenu() {
        while (true) {
            System.out.println("\nStudent Grade Management System");
            System.out.println("1. Student Management");
            System.out.println("2. Subject Management");
            System.out.println("3. Grade Management");
            System.out.println("4. Attendance Management");
            System.out.println("5. Generate Reports");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            try {
                switch (choice) {
                    case 1:
                        studentManagement();
                        break;
                    case 2:
                        subjectManagement();
                        break;
                    case 3:
                        gradeManagement();
                        break;
                    case 4:
                        attendanceManagement();
                        break;
                    case 5:
                        generateReports();
                        break;
                    case 6:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (SQLException e) {
                System.out.println("Database error: " + e.getMessage());
            }
        }
    }

    private static void studentManagement() throws SQLException {
        while (true) {
            System.out.println("\nStudent Management");
            System.out.println("1. Add Student");
            System.out.println("2. View All Students");
            System.out.println("3. Update Student");
            System.out.println("4. Delete Student");
            System.out.println("5. Back to Main Menu");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    studentManager.addStudent();
                    break;
                case 2:
                    studentManager.viewAllStudents();
                    break;
                case 3:
                    studentManager.updateStudent();
                    break;
                case 4:
                    studentManager.deleteStudent();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void subjectManagement() throws SQLException {
        while (true) {
            System.out.println("\nSubject Management");
            System.out.println("1. Add Subject");
            System.out.println("2. View All Subjects");
            System.out.println("3. Update Subject");
            System.out.println("4. Delete Subject");
            System.out.println("5. Back to Main Menu");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    subjectManager.addSubject();
                    break;
                case 2:
                    subjectManager.viewAllSubjects();
                    break;
                case 3:
                    subjectManager.updateSubject();
                    break;
                case 4:
                    subjectManager.deleteSubject();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void gradeManagement() throws SQLException {
        while (true) {
            System.out.println("\nGrade Management");
            System.out.println("1. Add Grade");
            System.out.println("2. View Student Grades");
            System.out.println("3. Calculate GPA");
            System.out.println("4. Back to Main Menu");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    gradeManager.addGrade();
                    break;
                case 2:
                    gradeManager.viewStudentGrades();
                    break;
                case 3:
                    gradeManager.calculateGPA();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void attendanceManagement() throws SQLException {
        while (true) {
            System.out.println("\nAttendance Management");
            System.out.println("1. Mark Attendance");
            System.out.println("2. View Attendance");
            System.out.println("3. Calculate Attendance Percentage");
            System.out.println("4. Back to Main Menu");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    attendanceManager.markAttendance();
                    break;
                case 2:
                    attendanceManager.viewAttendance();
                    break;
                case 3:
                    attendanceManager.calculateAttendancePercentage();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void generateReports() throws SQLException {
        while (true) {
            System.out.println("\nGenerate Reports");
            System.out.println("1. Student Performance Report");
            System.out.println("2. Attendance Summary Report");
            System.out.println("3. Back to Main Menu");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    generatePerformanceReport();
                    break;
                case 2:
                    generateAttendanceReport();
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void generatePerformanceReport() throws SQLException {
        studentManager.viewAllStudents();
        System.out.print("\nEnter student ID for performance report: ");
        int studentId = scanner.nextInt();
        scanner.nextLine();

        // Get student details
        String studentSql = "SELECT first_name, last_name FROM students WHERE student_id = ?";
        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(studentSql)) {
            pstmt.setInt(1, studentId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                System.out.printf("\nPerformance Report for %s %s%n", 
                    rs.getString("first_name"), 
                    rs.getString("last_name"));
            }
        }

        // Get grades
        gradeManager.viewStudentGrades();
        
        // Get attendance
        attendanceManager.calculateAttendancePercentage();
    }

    private static void generateAttendanceReport() throws SQLException {
        String sql = "SELECT s.first_name, s.last_name, " +
                    "COUNT(CASE WHEN a.status = 'Present' THEN 1 END) as present_count, " +
                    "COUNT(*) as total_classes " +
                    "FROM attendance a " +
                    "JOIN students s ON a.student_id = s.student_id " +
                    "GROUP BY s.student_id, s.first_name, s.last_name";
        
        try (Statement stmt = dbManager.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            System.out.println("\nAttendance Summary Report:");
            System.out.println("Student\t\tPresent\tTotal\tPercentage");
            while (rs.next()) {
                int present = rs.getInt("present_count");
                int total = rs.getInt("total_classes");
                double percentage = (present * 100.0) / total;
                System.out.printf("%s %s\t%d\t%d\t%.2f%%%n",
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    present,
                    total,
                    percentage);
            }
        }
    }
} 