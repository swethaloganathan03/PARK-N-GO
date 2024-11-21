import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class front extends JFrame {

    private JLabel heavyVehiclesCountLabel;
    private JLabel fourWheelersCountLabel;
    private JLabel eFourWheelersCountLabel;
    private JLabel twoWheelersCountLabel;
    private JLabel eTwoWheelersCountLabel;

    public front() {
        setTitle("Parking Management System");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(1, 2));

        JPanel leftPanel = new JPanel(new BorderLayout());
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        heavyVehiclesCountLabel = new JLabel();
        infoPanel.add(createItemPanel("Heavy Vehicles", heavyVehiclesCountLabel));

        fourWheelersCountLabel = new JLabel();
        infoPanel.add(createItemPanel("Four Wheelers", fourWheelersCountLabel));

        eFourWheelersCountLabel = new JLabel();
        infoPanel.add(createItemPanel("Electric Four Wheelers", eFourWheelersCountLabel));

        twoWheelersCountLabel = new JLabel();
        infoPanel.add(createItemPanel("Two Wheelers", twoWheelersCountLabel));

        eTwoWheelersCountLabel = new JLabel();
        infoPanel.add(createItemPanel("Electric Two Wheelers", eTwoWheelersCountLabel));

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshData();
            }
        });

        leftPanel.add(infoPanel, BorderLayout.CENTER);
        leftPanel.add(refreshButton, BorderLayout.SOUTH);

        JPanel rightPanel = new JPanel(new BorderLayout());
        JButton enterButton = new JButton("Enter");
        enterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open Main5.java
                Main5.main(null);
                setState(Frame.ICONIFIED); // Minimize the current frame
            }
        });

        rightPanel.add(enterButton, BorderLayout.SOUTH);

        add(leftPanel);
        add(rightPanel);

        refreshData();
    }

    private JPanel createItemPanel(String itemName, JLabel countLabel) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        JLabel itemNameLabel = new JLabel(itemName);
        itemNameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(itemNameLabel, BorderLayout.WEST);
        panel.add(countLabel, BorderLayout.EAST);
        return panel;
    }

    private void refreshData() {
        try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "system", "123root")) {
            // Retrieve and update available lot counts
            updateAvailableLotCount(connection, "HeavyVehicles", heavyVehiclesCountLabel);
            updateAvailableLotCount(connection, "FourWheeler", fourWheelersCountLabel);
            updateAvailableLotCount(connection, "EFourWheeler", eFourWheelersCountLabel);
            updateAvailableLotCount(connection, "TwoWheeler", twoWheelersCountLabel);
            updateAvailableLotCount(connection, "ETwoWheeler", eTwoWheelersCountLabel);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: Unable to refresh data", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateAvailableLotCount(Connection connection, String vehicleType, JLabel countLabel) throws SQLException {
        String query = "SELECT NO_OF_LOTS FROM garage WHERE VEHICLE_TYPE = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, vehicleType);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int totalLots = resultSet.getInt("NO_OF_LOTS");
                    int parkedVehiclesCount = getParkedCount(connection, vehicleType);
                    int availableLots = totalLots - parkedVehiclesCount;
                    countLabel.setText(Integer.toString(availableLots));
                }
            }
        }
    }

    private int getParkedCount(Connection connection, String vehicleType) throws SQLException {
        String query = 
            "SELECT COUNT(*) AS parked_count " +
            "FROM status s " +
            "WHERE s.STATUS_AVAIL = 'occupied' " +
            "AND EXISTS (" +
            "    SELECT 1 " +
            "    FROM garage g " +
            "    WHERE g.GARAGEID = s.GARAGE_ID " +
            "    AND g.FLOORID = s.FLOOR_ID " +
            "    AND g.VEHICLE_TYPE = ?" +
            ")";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, vehicleType);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("parked_count");
                }
            }
        }
        return 0; // Return 0 if no parked vehicles found
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            front frame = new front();
            frame.setVisible(true);
        });
    }
}
