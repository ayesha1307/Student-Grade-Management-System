import java.sql.*;
import java.util.Scanner;

public class Subject {
    private final Connection connection;
    private final Scanner scanner;

    public Subject(Connection connection) {
        this.connection = connection;
        this.scanner = new Scanner(System.in);
    }

    public void addSubject() throws SQLException {
        System.out.println("\nAdd New Subject");
        System.out.print("Enter subject name: ");
        String subjectName = scanner.nextLine();
        System.out.print("Enter credit hours: ");
        int creditHours = scanner.nextInt();
        scanner.nextLine();

        String sql = "INSERT INTO subjects (subject_name, credit_hours) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, subjectName);
            pstmt.setInt(2, creditHours);
            pstmt.executeUpdate();
            System.out.println("Subject added successfully!");
        }
    }

    public void viewAllSubjects() throws SQLException {
        String sql = "SELECT * FROM subjects";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("\nSubject List:");
            System.out.println("ID\tSubject\t\tCredit Hours");
            while (rs.next()) {
                System.out.printf("%d\t%s\t%d%n",
                    rs.getInt("subject_id"),
                    rs.getString("subject_name"),
                    rs.getInt("credit_hours"));
            }
        }
    }

    public void updateSubject() throws SQLException {
        viewAllSubjects();
        System.out.print("\nEnter subject ID to update: ");
        int subjectId = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter new subject name: ");
        String subjectName = scanner.nextLine();
        System.out.print("Enter new credit hours: ");
        int creditHours = scanner.nextInt();
        scanner.nextLine();

        String sql = "UPDATE subjects SET subject_name = ?, credit_hours = ? WHERE subject_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, subjectName);
            pstmt.setInt(2, creditHours);
            pstmt.setInt(3, subjectId);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Subject updated successfully!");
            } else {
                System.out.println("Subject not found!");
            }
        }
    }

    public void deleteSubject() throws SQLException {
        viewAllSubjects();
        System.out.print("\nEnter subject ID to delete: ");
        int subjectId = scanner.nextInt();
        scanner.nextLine();

        String sql = "DELETE FROM subjects WHERE subject_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, subjectId);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Subject deleted successfully!");
            } else {
                System.out.println("Subject not found!");
            }
        }
    }
} 