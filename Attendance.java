import java.sql.*;
import java.util.Scanner;

public class Attendance {
    private final Connection connection;
    private final Scanner scanner;

    public Attendance(Connection connection) {
        this.connection = connection;
        this.scanner = new Scanner(System.in);
    }

    public void markAttendance() throws SQLException {
        // Show available students
        showStudents();
        System.out.print("\nEnter student ID: ");
        int studentId = scanner.nextInt();
        scanner.nextLine();

        // Show available subjects
        showSubjects();
        System.out.print("Enter subject ID: ");
        int subjectId = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter date (YYYY-MM-DD): ");
        String date = scanner.nextLine();

        System.out.print("Enter status (Present/Absent): ");
        String status = scanner.nextLine();

        String sql = "INSERT INTO attendance (student_id, subject_id, date, status) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, studentId);
            pstmt.setInt(2, subjectId);
            pstmt.setString(3, date);
            pstmt.setString(4, status);
            pstmt.executeUpdate();
            System.out.println("Attendance marked successfully!");
        }
    }

    public void viewAttendance() throws SQLException {
        showStudents();
        System.out.print("\nEnter student ID to view attendance: ");
        int studentId = scanner.nextInt();
        scanner.nextLine();

        String sql = "SELECT s.subject_name, a.date, a.status " +
                    "FROM attendance a " +
                    "JOIN subjects s ON a.subject_id = s.subject_id " +
                    "WHERE a.student_id = ? " +
                    "ORDER BY a.date DESC";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, studentId);
            ResultSet rs = pstmt.executeQuery();
            
            System.out.println("\nAttendance Record:");
            System.out.println("Subject\t\tDate\t\tStatus");
            while (rs.next()) {
                System.out.printf("%s\t%s\t%s%n",
                    rs.getString("subject_name"),
                    rs.getString("date"),
                    rs.getString("status"));
            }
        }
    }

    public void calculateAttendancePercentage() throws SQLException {
        showStudents();
        System.out.print("\nEnter student ID to calculate attendance percentage: ");
        int studentId = scanner.nextInt();
        scanner.nextLine();

        String sql = "SELECT s.subject_name, " +
                    "COUNT(CASE WHEN a.status = 'Present' THEN 1 END) as present_count, " +
                    "COUNT(*) as total_classes " +
                    "FROM attendance a " +
                    "JOIN subjects s ON a.subject_id = s.subject_id " +
                    "WHERE a.student_id = ? " +
                    "GROUP BY s.subject_name";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, studentId);
            ResultSet rs = pstmt.executeQuery();
            
            System.out.println("\nAttendance Percentage:");
            System.out.println("Subject\t\tPresent\tTotal\tPercentage");
            while (rs.next()) {
                int present = rs.getInt("present_count");
                int total = rs.getInt("total_classes");
                double percentage = (present * 100.0) / total;
                System.out.printf("%s\t%d\t%d\t%.2f%%%n",
                    rs.getString("subject_name"),
                    present,
                    total,
                    percentage);
            }
        }
    }

    private void showStudents() throws SQLException {
        String sql = "SELECT student_id, first_name, last_name FROM students";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("\nAvailable Students:");
            System.out.println("ID\tName");
            while (rs.next()) {
                System.out.printf("%d\t%s %s%n",
                    rs.getInt("student_id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"));
            }
        }
    }

    private void showSubjects() throws SQLException {
        String sql = "SELECT subject_id, subject_name FROM subjects";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("\nAvailable Subjects:");
            System.out.println("ID\tSubject");
            while (rs.next()) {
                System.out.printf("%d\t%s%n",
                    rs.getInt("subject_id"),
                    rs.getString("subject_name"));
            }
        }
    }
} 