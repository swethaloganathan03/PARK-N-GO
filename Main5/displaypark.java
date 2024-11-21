import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;


public class displaypark extends JFrame implements ActionListener {
    private JComboBox<String> floorComboBox;
    private JPanel floorPanel;

    public displaypark() {
        super("Parking System");
        setLayout(new BorderLayout());

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));

        JPanel floorSelectionPanel = new JPanel();
        floorSelectionPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        floorSelectionPanel.add(new JLabel("Select Floor: "));
        floorComboBox = new JComboBox<>(new String[]{"1", "2", "3", "4", "5"});
        floorComboBox.addActionListener(this);
        floorSelectionPanel.add(floorComboBox);
        controlPanel.add(floorSelectionPanel);

        JButton displayButton = new JButton("Display Slot");
        displayButton.addActionListener(this);
        controlPanel.add(displayButton);

        floorPanel = new JPanel();
        floorPanel.setLayout(new GridLayout(1, 1));
        add(controlPanel, BorderLayout.EAST);
        add(floorPanel, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void setupFloorPanel(int floorNumber) {
        floorPanel.removeAll();
        String floorName = floorComboBox.getSelectedItem().toString();

        try {
            Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "system", "123root");
            String sql = "SELECT LOT, STATUS_AVAIL FROM status WHERE FLOOR_ID = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, floorNumber);
            ResultSet rs = pstmt.executeQuery();

            JPanel floor = new JPanel();
            floor.setBorder(BorderFactory.createTitledBorder(floorName));
            floor.setLayout(new GridLayout(10, 10));

            while (rs.next()) {
                int slotID = rs.getInt("LOT");
                String status = rs.getString("STATUS_AVAIL");
                JToggleButton slotButton = new JToggleButton(String.valueOf(slotID));
                if (status.equals("occupied")) {
                    slotButton.setBackground(Color.PINK);
                } else {
                    slotButton.setBackground(Color.BLACK);
                }
                slotButton.setOpaque(true);
                slotButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (slotButton.isSelected()) {
                            // Retrieve vehicle details from the database and display them
                            displayVehicleDetails(slotID);
                        }
                    }
                });
                floor.add(slotButton);
            }

            floorPanel.add(floor);
            revalidate();

            rs.close();
            pstmt.close();
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error occurred while fetching slots: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void displayVehicleDetails(int slotID) {
        // Connect to the database and retrieve vehicle details for the selected slot
        try {
            Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "system", "123root");
            String sql = "SELECT t.*, v.* FROM ticket t JOIN vehicle v ON t.VEHICLE_NO = v.VEHICLE_NO WHERE t.LOT_ID = ?";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, slotID);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int ticketNo = rs.getInt("TICKET_NO");
                String vehicleNo = rs.getString("VEHICLE_NO");
                Timestamp arrivalTime = rs.getTimestamp("ARRIVAL_TIME");
                Timestamp departureTime = rs.getTimestamp("DEPARTURE_TIME");
                int floorID = rs.getInt("FLOOR_ID");
                int lotID = rs.getInt("LOT_ID");
                int garageID = rs.getInt("GARAGE_ID");
            
                // Vehicle details
                String vehicleType = rs.getString("VEHICLE_TYPE");
                String electricVehicle = rs.getString("ELECTRIC_VEHICLE");
                String phoneNo = rs.getString("PHONE_NO");
            
                JOptionPane.showMessageDialog(this, "Ticket Details:\n" +
                        "Ticket No: " + ticketNo + "\n" +
                        "Vehicle No: " + vehicleNo + "\n" +
                        "Arrival Time: " + arrivalTime + "\n" +
                        "Departure Time: " + departureTime + "\n" +
                        "Floor ID: " + floorID + "\n" +
                        "Lot ID: " + lotID + "\n" +
                        "Garage ID: " + garageID + "\n\n" +
                        "Vehicle Details:\n" +
                        "Vehicle Type: " + vehicleType + "\n" +
                        "Electric Vehicle: " + electricVehicle + "\n" +
                        "Phone No: " + phoneNo, "Ticket and Vehicle Details", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "No vehicle parked in this slot.", "Vehicle Details", JOptionPane.INFORMATION_MESSAGE);
            }
            
            rs.close();
            pstmt.close();
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error occurred while fetching vehicle details: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void actionPerformed(ActionEvent e) {
        if ("Display Slot".equals(e.getActionCommand())) {
            try {
                int floorNumber = Integer.parseInt(floorComboBox.getSelectedItem().toString());
                setupFloorPanel(floorNumber);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid floor number.");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(displaypark::new);
    }
}
