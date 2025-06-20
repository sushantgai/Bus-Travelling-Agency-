import java.sql.*;
import java.util.Scanner;

public class Passenger {
    private static final String URL = "jdbc:mysql://localhost:3306/college";
    private static final String USER = "root";
    private static final String PASS = "Sushant@123";
    private Scanner scanner = new Scanner(System.in);

    public void addPassenger() {
        System.out.println("----------------Welcome to PASSENGER PAGE -------------------");
        
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        
        try {
            connection = DriverManager.getConnection(URL, USER, PASS);
            Bus b = new Bus();
            
            while (true) {
                connection.setAutoCommit(false);
                String query = "INSERT INTO Passenger(p_id, name, age, booked_date, bpoint, dpoint, bus_no, amount) VALUES (?,?,?,?,?,?,?,?)";
                preparedStatement = connection.prepareStatement(query);
                
                System.out.print("Enter Passenger ID: ");
                int p_id = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                System.out.print("Enter passenger name: ");
                String name = scanner.nextLine();

                System.out.print("Enter Age: ");
                int age = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                System.out.print("Enter Boarding point: ");
                String bpoint = scanner.nextLine();

                System.out.print("Enter Destination point: ");
                String dpoint = scanner.nextLine();

                System.out.print("Enter date (YYYY-MM-DD HH:MM:SS): ");
                String booked_date = scanner.nextLine();

                System.out.print("Enter number of passengers: ");
                int amount = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                // Check if bus is available
                boolean flag = b.isAvailable(booked_date, amount, bpoint, dpoint);
                if (flag) {
                    int bus_no = b.busno(booked_date, bpoint, dpoint);
                    if (bus_no != -1) {
                        preparedStatement.setInt(1, p_id);
                        preparedStatement.setString(2, name);
                        preparedStatement.setInt(3, age);
                        preparedStatement.setString(4, booked_date);
                        preparedStatement.setString(5, bpoint);
                        preparedStatement.setString(6, dpoint);
                        preparedStatement.setInt(7, bus_no);
                        preparedStatement.setInt(8, amount);

                        int result = preparedStatement.executeUpdate();
                        if (result > 0) {
                            connection.commit();
                            System.out.println("Passenger added successfully. Bus No: " + bus_no);
                        } else {
                            connection.rollback();
                            System.out.println("Failed to add passenger.");
                        }
                    } else {
                        System.out.println("Error retrieving bus number.");
                    }
                } else {
                    System.out.println("Bus is not available for the specified criteria.");
                }

                System.out.print("Do you want to add another passenger? (Y/N): ");
                String choice = scanner.nextLine();
                if (choice.equalsIgnoreCase("N")) {
                    break;
                }
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

    public void details() {
        System.out.println("--------------Details of Passengers -------------------------");
        
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DriverManager.getConnection(URL, USER, PASS);
            String query = "SELECT * FROM Passenger WHERE p_id = ?";
            preparedStatement = connection.prepareStatement(query);
            
            System.out.print("Enter Passenger ID: ");
            int p_id = scanner.nextInt();
            preparedStatement.setInt(1, p_id);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                System.out.println("\n--- Passenger Details ---");
                System.out.println("Passenger ID: " + resultSet.getInt("p_id"));
                System.out.println("Name: " + resultSet.getString("name"));
                System.out.println("Age: " + resultSet.getInt("age"));
                System.out.println("Booking Date: " + resultSet.getString("booked_date"));
                System.out.println("Boarding Point: " + resultSet.getString("bpoint"));
                System.out.println("Destination Point: " + resultSet.getString("dpoint"));
                System.out.println("Bus Number: " + resultSet.getInt("bus_no"));
                System.out.println("Number of Passengers: " + resultSet.getInt("amount"));
            } else {
                System.out.println("No passenger found with ID: " + p_id);
            }

        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
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