import java.sql.*;
import java.util.Scanner;

public class Student {
    private final Connection connection;
    private final Scanner scanner;

    public Student(Connection connection) {
        this.connection = connection;
        this.scanner = new Scanner(System.in);
    }

    public void addStudent() throws SQLException {
        System.out.println("\nAdd New Student");
        System.out.print("Enter first name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter last name: ");
        String lastName = scanner.nextLine();
        System.out.print("Enter date of birth (YYYY-MM-DD): ");
        String dob = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter phone number: ");
        String phone = scanner.nextLine();

        String sql = "INSERT INTO students (first_name, last_name, date_of_birth, email, phone) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, dob);
            pstmt.setString(4, email);
            pstmt.setString(5, phone);
            pstmt.executeUpdate();
            System.out.println("Student added successfully!");
        }
    }

    public void viewAllStudents() throws SQLException {
        String sql = "SELECT * FROM students";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("\nStudent List:");
            System.out.println("ID\tName\t\tEmail\t\tPhone");
            while (rs.next()) {
                System.out.printf("%d\t%s %s\t%s\t%s%n",
                    rs.getInt("student_id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("email"),
                    rs.getString("phone"));
            }
        }
    }

    public void updateStudent() throws SQLException {
        viewAllStudents();
        System.out.print("\nEnter student ID to update: ");
        int studentId = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter new first name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter new last name: ");
        String lastName = scanner.nextLine();
        System.out.print("Enter new email: ");
        String email = scanner.nextLine();
        System.out.print("Enter new phone number: ");
        String phone = scanner.nextLine();

        String sql = "UPDATE students SET first_name = ?, last_name = ?, email = ?, phone = ? WHERE student_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, email);
            pstmt.setString(4, phone);
            pstmt.setInt(5, studentId);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Student updated successfully!");
            } else {
                System.out.println("Student not found!");
            }
        }
    }

    public void deleteStudent() throws SQLException {
        viewAllStudents();
        System.out.print("\nEnter student ID to delete: ");
        int studentId = scanner.nextInt();
        scanner.nextLine();

        String sql = "DELETE FROM students WHERE student_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, studentId);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Student deleted successfully!");
            } else {
                System.out.println("Student not found!");
            }
        }
    }
} 