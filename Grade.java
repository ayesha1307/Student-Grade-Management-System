import java.sql.*;
import java.util.Scanner;

public class Grade {
    private final Connection connection;
    private final Scanner scanner;

    public Grade(Connection connection) {
        this.connection = connection;
        this.scanner = new Scanner(System.in);
    }

    public void addGrade() throws SQLException {
        // First, show available students
        showStudents();
        System.out.print("\nEnter student ID: ");
        int studentId = scanner.nextInt();
        scanner.nextLine();

        // Show available subjects
        showSubjects();
        System.out.print("Enter subject ID: ");
        int subjectId = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter grade value: ");
        double gradeValue = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Enter semester (e.g., Fall 2023): ");
        String semester = scanner.nextLine();

        String sql = "INSERT INTO grades (student_id, subject_id, grade_value, semester) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, studentId);
            pstmt.setInt(2, subjectId);
            pstmt.setDouble(3, gradeValue);
            pstmt.setString(4, semester);
            pstmt.executeUpdate();
            System.out.println("Grade added successfully!");
        }
    }

    public void viewStudentGrades() throws SQLException {
        showStudents();
        System.out.print("\nEnter student ID to view grades: ");
        int studentId = scanner.nextInt();
        scanner.nextLine();

        String sql = "SELECT s.subject_name, g.grade_value, g.semester " +
                    "FROM grades g " +
                    "JOIN subjects s ON g.subject_id = s.subject_id " +
                    "WHERE g.student_id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, studentId);
            ResultSet rs = pstmt.executeQuery();
            
            System.out.println("\nStudent Grades:");
            System.out.println("Subject\t\tGrade\tSemester");
            while (rs.next()) {
                System.out.printf("%s\t%.2f\t%s%n",
                    rs.getString("subject_name"),
                    rs.getDouble("grade_value"),
                    rs.getString("semester"));
            }
        }
    }

    public void calculateGPA() throws SQLException {
        showStudents();
        System.out.print("\nEnter student ID to calculate GPA: ");
        int studentId = scanner.nextInt();
        scanner.nextLine();

        String sql = "SELECT AVG(g.grade_value) as gpa, COUNT(*) as total_subjects " +
                    "FROM grades g " +
                    "WHERE g.student_id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, studentId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                double gpa = rs.getDouble("gpa");
                int totalSubjects = rs.getInt("total_subjects");
                System.out.printf("\nStudent GPA: %.2f (based on %d subjects)%n", gpa, totalSubjects);
            } else {
                System.out.println("No grades found for this student.");
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
        String sql = "SELECT subject_id, subject_name, credit_hours FROM subjects";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("\nAvailable Subjects:");
            System.out.println("ID\tSubject\t\tCredit Hours");
            while (rs.next()) {
                System.out.printf("%d\t%s\t%d%n",
                    rs.getInt("subject_id"),
                    rs.getString("subject_name"),
                    rs.getInt("credit_hours"));
            }
        }
    }
} 