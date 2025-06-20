import java.sql.*;
import java.util.Scanner;

public class Bus {
    private static final String URL = "jdbc:mysql://localhost:3306/college";
    private static final String USER = "root";
    private static final String PASS = "Sushant@123";

    public void addBuses() {
        System.out.println("-----------Welcome to BUS PAGE --------------- ");
        
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        Scanner scanner = null;
        
        try {
            connection = DriverManager.getConnection(URL, USER, PASS);
            connection.setAutoCommit(false);
            scanner = new Scanner(System.in);
            
            String query = "INSERT INTO Bus(bus_no, Date, capacity, spoint, epoint) VALUES(?,?,?,?,?)";
            preparedStatement = connection.prepareStatement(query);

            while (true) {
                System.out.print("Enter Bus no.: ");
                int bus_no = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                System.out.print("Enter Date (YYYY-MM-DD HH:MM:SS): ");
                String date = scanner.nextLine();

                System.out.print("Enter Capacity: ");
                int capacity = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                System.out.print("Enter Starting point of Bus: ");
                String spoint = scanner.nextLine();

                System.out.print("Enter Endpoint of Bus: ");
                String epoint = scanner.nextLine();

                preparedStatement.setInt(1, bus_no);
                preparedStatement.setString(2, date);
                preparedStatement.setInt(3, capacity);
                preparedStatement.setString(4, spoint);
                preparedStatement.setString(5, epoint);

                preparedStatement.addBatch();

                System.out.print("Do you want to add more Buses (Y/N)? ");
                String choice = scanner.nextLine();
                if (choice.equalsIgnoreCase("N")) {
                    break;
                }
            }

            int[] arr = preparedStatement.executeBatch();
            boolean allInserted = true;

            for (int count : arr) {
                if (count == 0) {
                    allInserted = false;
                    break;
                }
            }

            if (allInserted) {
                connection.commit();
                System.out.println("All buses are added successfully.");
            } else {
                connection.rollback();
                System.out.println("Some buses were not added. Transaction rolled back.");
            }

        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackEx) {
                    System.err.println("Rollback failed: " + rollbackEx.getMessage());
                }
            }
        } finally {
            // Close resources
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
    }

    public boolean isAvailable(String date, int capacity, String bpoint, String dpoint) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DriverManager.getConnection(URL, USER, PASS);
            String query = "SELECT Date, capacity, spoint, epoint FROM Bus WHERE Date = ? AND capacity >= ? AND spoint = ? AND epoint = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, date);
            preparedStatement.setInt(2, capacity);
            preparedStatement.setString(3, bpoint);
            preparedStatement.setString(4, dpoint);
            
            resultSet = preparedStatement.executeQuery();
            return resultSet.next();
            
        } catch (SQLException e) {
            System.err.println("Database error while checking availability: " + e.getMessage());
            return false;
        } finally {
            // Close resources
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
    }

    public int busno(String date, String spoint, String epoint) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DriverManager.getConnection(URL, USER, PASS);
            String query = "SELECT bus_no FROM Bus WHERE Date = ? AND spoint = ? AND epoint = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, date);
            preparedStatement.setString(2, spoint);
            preparedStatement.setString(3, epoint);
            
            resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                return resultSet.getInt("bus_no");
            } else {
                return -1; // Bus not found
            }

        } catch (SQLException e) {
            System.err.println("Database error while getting bus number: " + e.getMessage());
            return -1;
        } finally {
            // Close resources
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
    }
}