import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

public class BankingApp {
    public static void main(String[] args) {
        String url = "jdbc:oracle:thin:@localhost:1521:xe"; // Oracle connection URL
        String userName = "system";
        String password = "123root";

        try (Connection connection = DriverManager.getConnection(url, userName, password)) {
            createTables(connection);

            JOptionPane.showMessageDialog(null, "Tables created successfully",
                    "System Message", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error occurred: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void createTables(Connection connection) throws Exception {
        // Create bankingapplication table
        String createBankingApplicationTableSQL = "CREATE TABLE bankingapplication (" +
                "accno INT PRIMARY KEY, " +
                "description VARCHAR(255), " +
                "amount DECIMAL(10,2), " +
                "status VARCHAR(50))";
        executeUpdate(connection, createBankingApplicationTableSQL);

        // Create transactionlog table
        String createTransactionLogTableSQL = "CREATE TABLE transactionlog (" +
                "accno INT, " +
                "description VARCHAR(255), " +
                "amount DECIMAL(10,2), " +
                "timestamp TIMESTAMP, " +
                "status VARCHAR(50), " +
                "FOREIGN KEY (accno) REFERENCES bankingapplication(accno))";
        executeUpdate(connection, createTransactionLogTableSQL);

        // Insert default values into bankingapplication table
        String[] insertBankingAppDefaultValuesSQL = {
            "INSERT INTO bankingapplication (accno, description, amount, status) VALUES (101, 'Initial Deposit', 1000.00, 'Active')",
            "INSERT INTO bankingapplication (accno, description, amount, status) VALUES (102, 'Initial Deposit', 1500.00, 'Active')"
        };
        for (String sql : insertBankingAppDefaultValuesSQL) {
            executeUpdate(connection, sql);
        }

        // Insert default values into transactionlog table
        String[] insertTransactionLogDefaultValuesSQL = {
            "INSERT INTO transactionlog (accno, description, amount, timestamp, status) VALUES (101, 'Deposit', 500.00, SYSTIMESTAMP, 'Completed')",
            "INSERT INTO transactionlog (accno, description, amount, timestamp, status) VALUES (102, 'Withdrawal', 200.00, SYSTIMESTAMP, 'Completed')"
        };
        for (String sql : insertTransactionLogDefaultValuesSQL) {
            executeUpdate(connection, sql);
        }
    }

    private static void executeUpdate(Connection connection, String sql) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        }
    }
}
