import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQLExample {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/your_database";
        String username = "root";
        String password = "admin";

        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            
            createTable(connection);

            Scanner scanner = new Scanner(System.in);
            int choice;
            
            do {
                System.out.println("1. Add Student");
                System.out.println("2. Check Attendance");
                System.out.println("3. Add Attendance for a Day");
                System.out.println("0. Exit");
                System.out.print("Enter your choice: ");
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume the newline
                
                switch (choice) {
                    case 1:
                        System.out.print("Enter student name: ");
                        String name = scanner.nextLine();
                        System.out.print("Enter student ID: ");
                        int id = scanner.nextInt();
                        insertData(connection, name, id);
                        break;
                        
                    case 2:
                        retrieveData(connection);
                        break;
                        
                    case 3:
                        System.out.print("Enter student ID: ");
                        int studentId = scanner.nextInt();
                        scanner.nextLine(); // Consume the newline
                        System.out.print("Enter date (YYYY-MM-DD): ");
                        String date = scanner.nextLine();
                        recordAttendance(connection, studentId, date);
                        break;
                }
            } while (choice != 0);

            scanner.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createTable(Connection connection) throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS students (" +
                "id INT PRIMARY KEY AUTO_INCREMENT, " +
                "name VARCHAR(255) NOT NULL)";
        Statement statement = connection.createStatement();
        statement.executeUpdate(createTableSQL);
        statement.close();
        
        String createAttendanceTableSQL = "CREATE TABLE IF NOT EXISTS attendance (" +
                "id INT PRIMARY KEY AUTO_INCREMENT, " +
                "student_id INT NOT NULL, " +
                "date DATE NOT NULL)";
        Statement attendanceStatement = connection.createStatement();
        attendanceStatement.executeUpdate(createAttendanceTableSQL);
        attendanceStatement.close();
    }

    public static void insertData(Connection connection, String name, int id) throws SQLException {
        String sql = "INSERT INTO students (name) VALUES (?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, name);
        preparedStatement.executeUpdate();
        preparedStatement.close();
        System.out.println("Data inserted successfully.");
    }

    public static void retrieveData(Connection connection) throws SQLException {
        String sql = "SELECT * FROM students";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            System.out.println("ID: " + id + ", Name: " + name);
        }

        resultSet.close();
        statement.close();
    }
    
    public static void recordAttendance(Connection connection, int studentId, String date) throws SQLException {
        String sql = "INSERT INTO attendance (student_id, date) VALUES (?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, studentId);
        preparedStatement.setString(2, date);
        preparedStatement.executeUpdate();
        preparedStatement.close();
        System.out.println("Attendance recorded successfully.");
    }
}
