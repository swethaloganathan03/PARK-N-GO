import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

public class park {
    public static void main(String[] args) {
        String url = "jdbc:oracle:thin:@localhost:1521:xe"; // Oracle connection URL
        String userName = "system";
        String password = "123root";

        try (Connection connection = DriverManager.getConnection(url, userName, password)) {
            createTables(connection);
            insertStatusData(connection); // Insert status data

            JOptionPane.showMessageDialog(null, "Tables created and queries executed successfully",
                    "System Message", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error occurred: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void createTables(Connection connection) throws Exception {
        // Create garages table
        String createGarageTableSQL = "CREATE TABLE garage (garageid INT, floorid INT, no_of_lots INT, vehicle_type VARCHAR(255), PRIMARY KEY (garageid, floorid))";
        executeUpdate(connection, createGarageTableSQL);

        // Insert default values into garages table
        String[] insertGarageDefaultValuesSQL = {
            "INSERT INTO garage (garageid, floorid, no_of_lots, vehicle_type) VALUES (1, 1, 30, 'HeavyVehicles')",
            "INSERT INTO garage (garageid, floorid, no_of_lots, vehicle_type) VALUES (1, 2, 50, 'FourWheeler')",
            "INSERT INTO garage (garageid, floorid, no_of_lots, vehicle_type) VALUES (1, 3, 50, 'EFourWheeler')",
            "INSERT INTO garage (garageid, floorid, no_of_lots, vehicle_type) VALUES (1, 4, 100, 'TwoWheeler')",
            "INSERT INTO garage (garageid, floorid, no_of_lots, vehicle_type) VALUES (1, 5, 100, 'ETwoWheeler')"
        };
        for (String sql : insertGarageDefaultValuesSQL) {
            executeUpdate(connection, sql);
        }

        // Create fair table
        String createFairTableSQL = "CREATE TABLE fair (vehicle_type VARCHAR(255), amount DECIMAL(10,2))";
        executeUpdate(connection, createFairTableSQL);

        // Insert default values into fair table
        String[] insertFairDefaultValuesSQL = {
            "INSERT INTO fair (vehicle_type, amount) VALUES ('HeavyVehicles', 40.00)",
            "INSERT INTO fair (vehicle_type, amount) VALUES ('FourWheeler', 25.00)",
            "INSERT INTO fair (vehicle_type, amount) VALUES ('EFourWheeler', 35.00)",
            "INSERT INTO fair (vehicle_type, amount) VALUES ('TwoWheeler', 15.00)",
            "INSERT INTO fair (vehicle_type, amount) VALUES ('ETwoWheeler', 25.00)"
        };
        for (String sql : insertFairDefaultValuesSQL) {
            executeUpdate(connection, sql);
        }

        

        // Create ticket table
        String createTicketTableSQL = "CREATE TABLE ticket (ticket_no INT PRIMARY KEY,vehicle_no VARCHAR(50), " +
                                        "arrival_time TIMESTAMP, departure_time TIMESTAMP, " +
                                        "floor_id INT, lot_id INT,garage_id INT)";
        executeUpdate(connection, createTicketTableSQL);

        // Create vehicle table
        String createVehicleTableSQL = "CREATE TABLE vehicle (vehicle_no VARCHAR(50) PRIMARY KEY, vehicle_type VARCHAR(50), " +
                                        "electric_vehicle VARCHAR(50), phone_no VARCHAR(50))";
        executeUpdate(connection, createVehicleTableSQL);

        // Create status table
        String createStatusTableSQL = "CREATE TABLE status (garage_id INT, floor_id INT, lot INT, status_avail VARCHAR(50), PRIMARY KEY (garage_id, floor_id, lot))";
        executeUpdate(connection, createStatusTableSQL);
    }

    private static void executeUpdate(Connection connection, String sql) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        }
    }

    private static void insertStatusData(Connection connection) throws SQLException {
        // Insert status data using SQL statements
        String insertStatusDataSQL = "INSERT INTO status (garage_id, floor_id, lot, status_avail)\n" +
                                        "SELECT 1, 1, LEVEL, 'free' FROM DUAL CONNECT BY LEVEL <= 30\n" +
                                        "UNION ALL\n" +
                                        "SELECT 1, 2, LEVEL, 'free' FROM DUAL CONNECT BY LEVEL <= 50\n" +
                                        "UNION ALL\n" +
                                        "SELECT 1, 3, LEVEL, 'free' FROM DUAL CONNECT BY LEVEL <= 50\n" +
                                        "UNION ALL\n" +
                                        "SELECT 1, 4, LEVEL, 'free' FROM DUAL CONNECT BY LEVEL <= 100\n" +
                                        "UNION ALL\n" +
                                        "SELECT 1, 5, LEVEL, 'free' FROM DUAL CONNECT BY LEVEL <= 100";

        executeUpdate(connection, insertStatusDataSQL);
    }
}
