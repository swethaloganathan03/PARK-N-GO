import javax.swing.*;
import java.time.Duration;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import javax.imageio.ImageIO;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;






class ParkingLot {
    private String floor;
    private int lotNumber;
    private String status;

    public ParkingLot(String floor, int lotNumber, String status) {
        this.floor = floor;
        this.lotNumber = lotNumber;
        this.status = status;
    }

    public String getFloor() {
        return floor;
    }

    public int getLotNumber() {
        return lotNumber;
    }

    public String getStatus() {
        return status;
    }
}

public class Main5 {
    // Define the list of available parking lots
    private static List<ParkingLot> availableLots = new ArrayList<>();

    static {
        // Sample data for available parking lots
        availableLots.add(new ParkingLot("1", 1, "parked"));
        availableLots.add(new ParkingLot("1", 2, "parked"));
        availableLots.add(new ParkingLot("4", 1, "free"));
        availableLots.add(new ParkingLot("1", 2, "free"));
        // Add more as needed
    }

    
    private static JFrame loginFrame;
    private static JFrame detailsFrame;
    private static JFrame exitFrame;
    private static JTextField phoneField;
    private static JTextField vehicleNumberField;
    private static JComboBox<String> vehicleTypeComboBox;
    private static JCheckBox chargingFacilityCheckBox;
    //private static int tokenCounter = 0;
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main5::createLoginPage);
    }
    
    private static void createLoginPage() {
        loginFrame = new JFrame("Login");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(800, 600);
        loginFrame.setLocationRelativeTo(null);

        JPanel contentPane = new BackgroundPanel("firstbg.jpg");
        contentPane.setLayout(new BorderLayout());

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonsPanel.setOpaque(false);
        contentPane.add(buttonsPanel, BorderLayout.CENTER);

        JButton parkButton = createStyledButton("PARK");
        parkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openDetailsPanel();
            }
        });
        buttonsPanel.add(parkButton);

        JButton exitButton = createStyledButton("EXIT");
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openExitPanel();
            }
        });
        
        buttonsPanel.add(exitButton);
        contentPane.add(buttonsPanel, BorderLayout.CENTER);
        JButton displayVehicleButton = createStyledButton("DISPLAY VEHICLE");
        displayVehicleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new displayvehicle(); // Create an instance of the displayvehicle class
            }
        });
        buttonsPanel.add(displayVehicleButton);
        /* JButton displayVehicleButton = createStyledButton("DISPLAY VEHICLE");
        displayVehicleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement what happens when the "DISPLAY VEHICLE" button be clicked
                JOptionPane.showMessageDialog(loginFrame,"");
            }
        });*/



        JButton adminButton = createStyledButton("ADMIN");
        adminButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openAdminLoginPanel(); // Open admin login panel when admin button is clicked
            }
        });

        JPanel adminPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        adminPanel.setOpaque(false);
        adminPanel.add(adminButton);

        contentPane.add(adminPanel, BorderLayout.SOUTH);

        loginFrame.setContentPane(contentPane);
        loginFrame.setVisible(true);
    }

    private static JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Audela", Font.BOLD, 18));
        button.setForeground(Color.WHITE);
        button.setBackground(Color.BLACK);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setFocusPainted(false);
        return button;
    }

    private static void openDetailsPanel() {
        detailsFrame = new JFrame("Vehicle Details");
        detailsFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        detailsFrame.setSize(800, 600);
        detailsFrame.setLocationRelativeTo(null);

        JPanel contentPane = new BackgroundPanel("back.jpg");
        contentPane.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        JPanel ticketPanel = new JPanel();
        ticketPanel.setLayout(new BoxLayout(ticketPanel, BoxLayout.Y_AXIS));
        ticketPanel.setBorder(createRoundedBorder());

        JLabel titleLabel = new JLabel("   FIND YOUR SPOT  ");
        titleLabel.setFont(new Font("Audela", Font.BOLD, 28));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        ticketPanel.add(Box.createVerticalStrut(20));
        ticketPanel.add(titleLabel);
        ticketPanel.add(Box.createVerticalStrut(20));

        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new GridLayout(4, 1, 10, 10));

        JLabel phoneLabel = new JLabel("Phone Number:");
        phoneLabel.setFont(new Font("Audela", Font.PLAIN, 18));
        detailsPanel.add(phoneLabel);
        phoneField = createTextField();
        detailsPanel.add(phoneField);

        JLabel vehicleNumberLabel = new JLabel("Vehicle number:");
        vehicleNumberLabel.setFont(new Font("Audela", Font.PLAIN, 18));
        detailsPanel.add(vehicleNumberLabel);
        vehicleNumberField = createTextField();
        detailsPanel.add(vehicleNumberField);

        JLabel vehicleTypeLabel = new JLabel("Vehicle Type:");
        vehicleTypeLabel.setFont(new Font("Audela", Font.PLAIN, 18));
        detailsPanel.add(vehicleTypeLabel);
        String[] vehicleTypes = {"HeavyVehicles", "FourWheeler", "EFourWheeler", "TwoWheeler", "ETwoWheeler"};
        vehicleTypeComboBox = new JComboBox<>(vehicleTypes);
        detailsPanel.add(vehicleTypeComboBox);

        JLabel chargingFacilityLabel = new JLabel("Charging Facility:");
        chargingFacilityLabel.setFont(new Font("Audela", Font.PLAIN, 18));
        detailsPanel.add(chargingFacilityLabel);
        chargingFacilityCheckBox = new JCheckBox("Required");
        chargingFacilityCheckBox.setFont(new Font("Audela", Font.PLAIN, 18));
        detailsPanel.add(chargingFacilityCheckBox);

        ticketPanel.add(detailsPanel);
        ticketPanel.add(Box.createVerticalStrut(40));

        JButton generateTokenButton = new JButton("GENERATE TOKEN");
        generateTokenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String phone = phoneField.getText();
                String vehicleNumber = vehicleNumberField.getText();
                String vehicleType = (String) vehicleTypeComboBox.getSelectedItem();
                String chargingFacility = chargingFacilityCheckBox.isSelected() ? "Required" : "Not Required";
                if (!phone.isEmpty() && !vehicleNumber.isEmpty()) {
                    generateToken(phone, vehicleNumber, vehicleType, chargingFacility);
                } else {
                    JOptionPane.showMessageDialog(detailsFrame, "PLEASE FILL IN THE DETAILS");
                }
            }
        });
        ticketPanel.add(generateTokenButton);

        contentPane.add(ticketPanel, gbc);

        detailsFrame.setContentPane(contentPane);
        detailsFrame.setVisible(true);
    }

    private static JTextField createTextField() {
        JTextField textField = new JTextField();
        textField.setFont(new Font("Audela", Font.PLAIN, 18));
        return textField;
    }

    private static void openLoginPage() {
        loginFrame.setVisible(true);
    }



    // Database connection details
    private static final String DB_URL = "jdbc:oracle:thin:@localhost:1521:xe";
    private static final String DB_USER = "system";
    private static final String DB_PASSWORD = "123root";

    private static void generateToken(String phone, String vehicleNumber, String vehicleType, String chargingFacility) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            // Query the garage table to find available floors with the corresponding vehicle type
            String findAvailableFloorsQuery = "SELECT FLOORID, NO_OF_LOTS FROM garage WHERE VEHICLE_TYPE = ?";
            PreparedStatement findAvailableFloorsStmt = connection.prepareStatement(findAvailableFloorsQuery);
            findAvailableFloorsStmt.setString(1, vehicleType);
            ResultSet availableFloorsResult = findAvailableFloorsStmt.executeQuery();
    
            int allocatedFloor = 0;
            int allocatedLot = 0;
            int garageId = 1; // Default garage ID
    
            if (availableFloorsResult.next()) {
                allocatedFloor = availableFloorsResult.getInt("FLOORID");
                int totalLots = availableFloorsResult.getInt("NO_OF_LOTS");
    
                // Query the status table to find the first available lot in the allocated floor
                String findAvailableLotQuery = "SELECT LOT FROM status WHERE FLOOR_ID = ? AND STATUS_AVAIL = 'free'";
                PreparedStatement findAvailableLotStmt = connection.prepareStatement(findAvailableLotQuery);
                findAvailableLotStmt.setInt(1, allocatedFloor);
                ResultSet availableLotResult = findAvailableLotStmt.executeQuery();
    
                if (availableLotResult.next()) {
                    allocatedLot = availableLotResult.getInt("LOT");
    
                    // Update the status of the allocated lot to "occupied"
                    String updateStatusQuery = "UPDATE status SET STATUS_AVAIL = 'occupied' WHERE FLOOR_ID = ? AND LOT = ?";
                    PreparedStatement updateStatusStmt = connection.prepareStatement(updateStatusQuery);
                    updateStatusStmt.setInt(1, allocatedFloor);
                    updateStatusStmt.setInt(2, allocatedLot);
                    updateStatusStmt.executeUpdate();
                    updateStatusStmt.close();
                } else {
                    JOptionPane.showMessageDialog(detailsFrame, "No available lots in the selected floor.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                availableLotResult.close();
                findAvailableLotStmt.close();
            } else {
                JOptionPane.showMessageDialog(detailsFrame, "No available floors for the selected vehicle type.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            availableFloorsResult.close();
            findAvailableFloorsStmt.close();
    
            LocalDateTime arrivalTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
            // Generate a random 5-digit ticket number
            int newTicketNumber = generateRandomTicketNumber();
    
            // Insert the new ticket into the database
            String insertTicketQuery = "INSERT INTO ticket (ticket_no, vehicle_no, arrival_time, floor_id, lot_id, garage_id) " +
                    "VALUES (?, ?, TO_TIMESTAMP(?, 'YYYY-MM-DD HH24:MI:SS'), ?, ?, ?)";
            PreparedStatement insertTicketStmt = connection.prepareStatement(insertTicketQuery);
            insertTicketStmt.setInt(1, newTicketNumber);
            insertTicketStmt.setString(2, vehicleNumber);
            insertTicketStmt.setString(3, arrivalTime.format(formatter));
            insertTicketStmt.setInt(4, allocatedFloor);
            insertTicketStmt.setInt(5, allocatedLot);
            insertTicketStmt.setInt(6, garageId);
            insertTicketStmt.executeUpdate();
            insertTicketStmt.close();
    
            // Insert the vehicle details into the vehicle table
            String insertVehicleQuery = "INSERT INTO vehicle (VEHICLE_NO, VEHICLE_TYPE, PHONE_NO , ELECTRIC_VEHICLE) VALUES (?, ?, ?,?)";
            PreparedStatement insertVehicleStmt = connection.prepareStatement(insertVehicleQuery);
            insertVehicleStmt.setString(1, vehicleNumber);
            insertVehicleStmt.setString(2, vehicleType);
            insertVehicleStmt.setString(3, phone);
            insertVehicleStmt.setString(4, chargingFacility);
            insertVehicleStmt.executeUpdate();
            insertVehicleStmt.close();
    
            // Show a message dialog with the generated token
            String token = "Token Number: " + newTicketNumber +
                    "\nPhone Number: " + phone +
                    "\nVehicle Number: " + vehicleNumber +
                    "\nVehicle Type: " + vehicleType +
                    "\nCharging Facility: " + chargingFacility +
                    "\nArrival Time: " + arrivalTime.format(formatter) +
                    "\nAllocated Floor: " + allocatedFloor +
                    "\nAllocated Lot: " + allocatedLot;
            JOptionPane.showMessageDialog(detailsFrame, token, "Token Generated", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(detailsFrame, "Error occurred while generating token: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private static int generateRandomTicketNumber() {
        Random random = new Random();
        return random.nextInt(90000) + 10000; // Generates a random number between 10000 and 99999
    }
    
    


    private static void openAdminLoginPanel() {
        JFrame adminLoginFrame = new JFrame("Admin Login");
        adminLoginFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        adminLoginFrame.setSize(800, 600);
        adminLoginFrame.setLocationRelativeTo(null);

        JPanel contentPane = new BackgroundPanel("adminbg.jpg");
        contentPane.setLayout(new BorderLayout());

        // Panel for the title label
        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        JLabel titleLabel = new JLabel("ADMIN LOGIN ");
        titleLabel.setFont(new Font("Audela", Font.BOLD, 40));
        titleLabel.setForeground(Color.gray);
        titlePanel.add(titleLabel);

        // Add the title panel above the input panel
        contentPane.add(titlePanel, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.RELATIVE;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel adminIdLabel = new JLabel("ADMIN ID:");
        adminIdLabel.setFont(new Font("Audela", Font.BOLD, 20));
        adminIdLabel.setForeground(Color.black);
        inputPanel.add(adminIdLabel, gbc);

        JTextField adminIdField = new JTextField(20);
        adminIdField.setFont(new Font("Audela", Font.PLAIN, 20));
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        inputPanel.add(adminIdField, gbc);

        JLabel adminPasswordLabel = new JLabel("PASSWORD:");
        adminPasswordLabel.setFont(new Font("Audela", Font.BOLD, 20));
        adminPasswordLabel.setForeground(Color.black);
        gbc.gridwidth = GridBagConstraints.RELATIVE;
        inputPanel.add(adminPasswordLabel, gbc);

        JPasswordField adminPasswordField = new JPasswordField(20);
        adminPasswordField.setFont(new Font("Audela", Font.PLAIN, 20));
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        inputPanel.add(adminPasswordField, gbc);

        contentPane.add(inputPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);

        JButton loginButton = new JButton("LOGIN");
        loginButton.setFont(new Font("Audela", Font.BOLD, 20));
        loginButton.setBackground(Color.BLACK);
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setPreferredSize(new Dimension(150, 50));
        loginButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String adminId = adminIdField.getText();
                String adminPassword = new String(adminPasswordField.getPassword());

                // Here be yer authentication logic, me hearty!
                // Check if adminId and adminPassword match some predefined values

                if (adminId.equals("admin") && adminPassword.equals("admin123")) {
                    // Successful login
                    adminLoginFrame.dispose(); // Close the admin login frame
                    openAdminPanel(); // Open admin panel
                } else {
                    // Failed login
                    JOptionPane.showMessageDialog(adminLoginFrame, "Invalid credentials. Please try again.", "Login Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        buttonPanel.add(loginButton);
        contentPane.add(buttonPanel, BorderLayout.SOUTH);

        adminLoginFrame.setContentPane(contentPane);
        adminLoginFrame.setVisible(true);
    }


    
    


    private static void openAdminPanel() {
        JFrame adminPanelFrame = new JFrame("Admin Panel");
        adminPanelFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        adminPanelFrame.setSize(1000, 600);
        adminPanelFrame.setLocationRelativeTo(null);
    
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBackground(Color.blue); // Set background color to blue
    
        JPanel optionsPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        optionsPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        optionsPanel.setBackground(Color.pink); // Set background color to pink
    
        // Button to check available slots
        JButton checkAvailableSlotsButton = createStyledButton("CHECK AVAILABLE SLOTS");
        checkAvailableSlotsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement logic to check available parking slots for all floors
                try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                    String query = "SELECT FLOOR_ID, COUNT(*) AS available_slots " +
                                   "FROM status " +
                                   "WHERE STATUS_AVAIL = 'free' " +
                                   "GROUP BY FLOOR_ID";
                    PreparedStatement statement = connection.prepareStatement(query);
                    ResultSet resultSet = statement.executeQuery();
                    StringBuilder message = new StringBuilder("Available Parking Slots:\n");
                    boolean hasData = false;
                    while (resultSet.next()) {
                        int floorId = resultSet.getInt("FLOOR_ID");
                        int availableSlots = resultSet.getInt("available_slots");
                        message.append("Floor ").append(floorId).append(": ").append(availableSlots).append(" slots\n");
                        hasData = true;
                    }
                    if (hasData) {
                        JOptionPane.showMessageDialog(adminPanelFrame, message.toString(), "Check Available Slots", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(adminPanelFrame, "No data available.", "Check Available Slots", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(adminPanelFrame, "Error occurred while fetching available slots: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        optionsPanel.add(checkAvailableSlotsButton);
    
        // Button to check vehicle details
        JButton checkVehicleDetailsButton = createStyledButton("CHECK VEHICLE DETAILS");
        checkVehicleDetailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement logic to check vehicle details
                String vehicleNo = JOptionPane.showInputDialog(adminPanelFrame, "Enter Vehicle Number:");
                if (vehicleNo != null && !vehicleNo.isEmpty()) {
                    try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                        String query = "SELECT * FROM vehicle WHERE VEHICLE_NO = ?";
                        PreparedStatement statement = connection.prepareStatement(query);
                        statement.setString(1, vehicleNo);
                        ResultSet resultSet = statement.executeQuery();
                        StringBuilder message = new StringBuilder();
                        boolean hasData = false;
                        while (resultSet.next()) {
                            message.append("Vehicle Number: ").append(resultSet.getString("VEHICLE_NO")).append("\n");
                            message.append("Vehicle Type: ").append(resultSet.getString("VEHICLE_TYPE")).append("\n");
                            message.append("Charging facility: ").append(resultSet.getString("ELECTRIC_VEHICLE")).append("\n");
                            message.append("Phone Number: ").append(resultSet.getString("PHONE_NO")).append("\n");
                            message.append("\n");
                            hasData = true;
                        }
                        if (hasData) {
                            query = "SELECT * FROM ticket WHERE VEHICLE_NO = ?";
                            statement = connection.prepareStatement(query);
                            statement.setString(1, vehicleNo);
                            resultSet = statement.executeQuery();
                            while (resultSet.next()) {
                                message.append("Ticket Number: ").append(resultSet.getString("TICKET_NO")).append("\n");
                                message.append("Arrival Time: ").append(resultSet.getString("ARRIVAL_TIME")).append("\n");
                                String departureTime = resultSet.getString("DEPARTURE_TIME");
                                message.append("Departure Time: ").append(departureTime != null ? departureTime : "Not Departed Yet").append("\n");
                                message.append("Floor ID: ").append(resultSet.getString("FLOOR_ID")).append("\n");
                                message.append("Lot ID: ").append(resultSet.getString("LOT_ID")).append("\n");
                                message.append("Garage ID: ").append(resultSet.getString("GARAGE_ID")).append("\n");
                                message.append("Amount: ").append(resultSet.getString("AMOUNT")).append("\n");
                                message.append("No of hours parked: ").append(resultSet.getString("DURATION_HOURS")).append("\n");
                                message.append("\n");
                            }
                            JOptionPane.showMessageDialog(adminPanelFrame, message.toString(), "Vehicle Details", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(adminPanelFrame, "Vehicle not found.", "Vehicle Details", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(adminPanelFrame, "Error occurred while fetching vehicle details: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        optionsPanel.add(checkVehicleDetailsButton);
    
        /* // Button to check number of vehicles
        JButton checkNumberOfVehiclesButton = createStyledButton("CHECK NUMBER OF VEHICLES");
        checkNumberOfVehiclesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement logic to check number of vehicles based on vehicle type
                String vehicleType = JOptionPane.showInputDialog(adminPanelFrame, "Enter Vehicle Type:");
                if (vehicleType != null && !vehicleType.isEmpty()) {
                    try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                        String query = "SELECT COUNT(*) AS vehicle_count FROM vehicle WHERE VEHICLE_TYPE = ?";
                        PreparedStatement statement = connection.prepareStatement(query);
                        statement.setString(1, vehicleType);
                        ResultSet resultSet = statement.executeQuery();
                        if (resultSet.next()) {
                            int vehicleCount = resultSet.getInt("vehicle_count");
                            JOptionPane.showMessageDialog(adminPanelFrame, "Number of " + vehicleType + " vehicles: " + vehicleCount, "Vehicle Count", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(adminPanelFrame, "No vehicles of type " + vehicleType + " found.", "Vehicle Count", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(adminPanelFrame, "Error occurred while fetching vehicle count: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(adminPanelFrame, "Please enter a vehicle type.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        optionsPanel.add(checkNumberOfVehiclesButton);*/
    

      // Button to check number of vehicles
JButton checkNumberOfVehiclesButton = createStyledButton("CHECK NUMBER OF VEHICLES");
checkNumberOfVehiclesButton.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        // Implement logic to check number of vehicles based on vehicle type
        String vehicleType = JOptionPane.showInputDialog(adminPanelFrame, "Enter Vehicle Type:");
        if (vehicleType != null && !vehicleType.isEmpty()) {
            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                // Query to count the number of occupied lots for the given vehicle type using subqueries
                String query = 
                    "SELECT COUNT(*) AS occupied_count " +
                    "FROM status s " +
                    "WHERE s.STATUS_AVAIL = 'occupied' " +
                    "AND EXISTS (" +
                    "    SELECT 1 " +
                    "    FROM garage g " +
                    "    WHERE g.GARAGEID = s.GARAGE_ID " +
                    "    AND g.FLOORID = s.FLOOR_ID " +
                    "    AND g.VEHICLE_TYPE = ?" +
                    ")";
                
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, vehicleType);
                ResultSet resultSet = statement.executeQuery();
                
                if (resultSet.next()) {
                    int occupiedCount = resultSet.getInt("occupied_count");
                    JOptionPane.showMessageDialog(adminPanelFrame, "Number of occupied lots for " + vehicleType + " vehicles: " + occupiedCount, "Occupied Lots Count", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(adminPanelFrame, "No occupied lots found for vehicle type " + vehicleType + ".", "Occupied Lots Count", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(adminPanelFrame, "Error occurred while fetching occupied lots count: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(adminPanelFrame, "Please enter a vehicle type.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
});
optionsPanel.add(checkNumberOfVehiclesButton);

        // Button to check total amount
        JButton checkTotalAmountButton = createStyledButton("CHECK TOTAL AMOUNT");
        checkTotalAmountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement logic to check total amount
                try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                    String query = "SELECT SUM(AMOUNT) AS total_amount FROM ticket";
                    PreparedStatement statement = connection.prepareStatement(query);
                    ResultSet resultSet = statement.executeQuery();
                    if (resultSet.next()) {
                        double totalAmount = resultSet.getDouble("total_amount"); // Use the alias "
                    
JOptionPane.showMessageDialog(adminPanelFrame, "Total Amount: " + totalAmount, "Total Amount", JOptionPane.INFORMATION_MESSAGE);
} else {
JOptionPane.showMessageDialog(adminPanelFrame, "No data available.", "Total Amount", JOptionPane.INFORMATION_MESSAGE);
}
} catch (SQLException ex) {
ex.printStackTrace();
JOptionPane.showMessageDialog(adminPanelFrame, "Error occurred while fetching total amount: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
}
}
});
optionsPanel.add(checkTotalAmountButton);

// Button to find frequently parked vehicles
JButton frequentlyParkedVehicleButton = createStyledButton("FREQUENTLY PARKED VEHICLES");
frequentlyParkedVehicleButton.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        // Implement logic to find frequently parked vehicles
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT vehicle_no, COUNT(*) AS park_count " +
                           "FROM ticket " +
                           "GROUP BY vehicle_no " +
                           "ORDER BY park_count DESC " +
                           "FETCH FIRST 1 ROW ONLY"; // Fetch only the first row
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String vehicleNo = resultSet.getString("vehicle_no");
                int parkCount = resultSet.getInt("park_count");
                JOptionPane.showMessageDialog(adminPanelFrame, "Most Frequently Parked Vehicle:\n" + "vehicle no : " + vehicleNo + " parked " + parkCount + " times", "Frequently Parked Vehicle", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(adminPanelFrame, "No data available.", "Frequently Parked Vehicles", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(adminPanelFrame, "Error occurred while fetching frequently parked vehicles: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
});
optionsPanel.add(frequentlyParkedVehicleButton);

// Button to check parked vehicles on a specific floor
JButton checkParkedVehiclesButton = createStyledButton("CHECK PARKED VEHICLES");
checkParkedVehiclesButton.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        new displaypark();
    }
});
optionsPanel.add(checkParkedVehiclesButton);

// Button to logout
JButton logoutButton = createStyledButton("LOGOUT");
logoutButton.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        // Close the admin panel and go back to login page
        adminPanelFrame.dispose();
        openLoginPage();
    }
});
optionsPanel.add(logoutButton);

contentPane.add(optionsPanel, BorderLayout.CENTER);

adminPanelFrame.setContentPane(contentPane);
adminPanelFrame.setVisible(true);
    }



    


    private static void openExitPanel() {
        exitFrame = new JFrame("Exit Parking");
        exitFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        exitFrame.setSize(800, 600);
        exitFrame.setLocationRelativeTo(null);
    
        JPanel contentPane = new BackgroundPanel("back.jpg");
        contentPane.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
    
        JPanel exitPanel = new JPanel();
        exitPanel.setLayout(new BoxLayout(exitPanel, BoxLayout.Y_AXIS));
        exitPanel.setBorder(createRoundedBorder());
    
        JLabel titleLabel = new JLabel("   TAKE YOUR VEHICLE  ");
        titleLabel.setFont(new Font("Audela", Font.BOLD, 28));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitPanel.add(titleLabel);
    
        exitPanel.add(Box.createVerticalStrut(20));
    
        JLabel ticketNumberLabel = new JLabel("Ticket Number:");
        ticketNumberLabel.setFont(new Font("Audela", Font.PLAIN, 18));
        ticketNumberLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Align center
        exitPanel.add(ticketNumberLabel);
    
        JTextField ticketNumberField = new JTextField();
        ticketNumberField.setFont(new Font("Audela", Font.PLAIN, 18));
        ticketNumberField.setMaximumSize(new Dimension(300, 40));
        ticketNumberField.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitPanel.add(ticketNumberField);
    
        exitPanel.add(Box.createVerticalStrut(20));
    
        /* // Button to check ticket
        JButton checkTicketButton = new JButton("CHECK TICKET");
        checkTicketButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        checkTicketButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String ticketNumber = ticketNumberField.getText();
                // Here you can validate the ticket number and fetch vehicle details
                try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                    // Query to retrieve arrival time
                    String ticketQuery = "SELECT ARRIVAL_TIME, VEHICLE_NO FROM ticket WHERE TICKET_NO = ?";
                    PreparedStatement ticketStmt = connection.prepareStatement(ticketQuery);
                    ticketStmt.setString(1, ticketNumber);
                    ResultSet ticketResult = ticketStmt.executeQuery();
    
                    if (ticketResult.next()) {
                        Timestamp arrivalTime = ticketResult.getTimestamp("ARRIVAL_TIME");
                        String vehicleNo = ticketResult.getString("VEHICLE_NO");
    
                        if (arrivalTime != null && vehicleNo != null) {
                            // Get the current time as the departure time
                            LocalDateTime departureLocalDateTime = LocalDateTime.now();
    
                            // Calculate duration in hours
                            long durationHours = Duration.between(arrivalTime.toLocalDateTime(), departureLocalDateTime).toHours();
                            
    
                            // Query to retrieve vehicle type
                            String vehicleTypeQuery = "SELECT VEHICLE_TYPE FROM vehicle WHERE VEHICLE_NO = ?";
                            PreparedStatement vehicleTypeStmt = connection.prepareStatement(vehicleTypeQuery);
                            vehicleTypeStmt.setString(1, vehicleNo);
                            ResultSet vehicleTypeResult = vehicleTypeStmt.executeQuery();
    
                            if (vehicleTypeResult.next()) {
                                String vehicleType = vehicleTypeResult.getString("VEHICLE_TYPE");
    
                                // Query to retrieve parking fee per hour from the 'fair' table based on vehicle type
                                String feeQuery = "SELECT AMOUNT FROM fair WHERE VEHICLE_TYPE = ?";
                                PreparedStatement feeStmt = connection.prepareStatement(feeQuery);
                                feeStmt.setString(1, vehicleType);
                                ResultSet feeResult = feeStmt.executeQuery();
    
                                if (feeResult.next()) {
                                    double parkingFeePerHour = feeResult.getDouble("AMOUNT");
    
                                    // Calculate total parking fee
                                    double totalParkingFee = durationHours * parkingFeePerHour;
    
                                    // Update the ticket with the departure time, duration hours, and amount
                                    String updateQuery = "UPDATE ticket SET DEPARTURE_TIME = ?, DURATION_HOURS = ?, AMOUNT = ? WHERE TICKET_NO = ?";
                                    PreparedStatement updateStmt = connection.prepareStatement(updateQuery);
                                    updateStmt.setTimestamp(1, Timestamp.valueOf(departureLocalDateTime));
                                    updateStmt.setLong(2, durationHours);
                                    updateStmt.setDouble(3, totalParkingFee);
                                    updateStmt.setString(4, ticketNumber);
                                    updateStmt.executeUpdate();


                                     // After updating the ticket table with departure time, duration, and amount
try {
    // Retrieve the garage ID, floor ID, and lot ID from the ticket table
    String garageQuery = "SELECT GARAGE_ID, FLOOR_ID, LOT_ID FROM ticket WHERE TICKET_NO = ?";
    PreparedStatement garageStmt = connection.prepareStatement(garageQuery);
    garageStmt.setString(1, ticketNumber);
    ResultSet garageResult = garageStmt.executeQuery();

    if (garageResult.next()) {
        int garageId = garageResult.getInt("GARAGE_ID");
        int floorId = garageResult.getInt("FLOOR_ID");
        int lotId = garageResult.getInt("LOT_ID");

        // Update the status table for the respective parking spot to "free"
        String updateStatusQuery = "UPDATE status SET STATUS_AVAIL = 'free' WHERE GARAGE_ID = ? AND FLOOR_ID = ? AND LOT = ?";
        PreparedStatement updateStatusStmt = connection.prepareStatement(updateStatusQuery);
        updateStatusStmt.setInt(1, garageId);
        updateStatusStmt.setInt(2, floorId);
        updateStatusStmt.setInt(3, lotId);
        int rowsUpdated = updateStatusStmt.executeUpdate();

        if (rowsUpdated > 0) {
            System.out.println("Status updated for parking spot to 'free'");
        } else {
            System.out.println("No rows updated in status table");
        }
    } else {
        JOptionPane.showMessageDialog(exitFrame, "Ticket information is incomplete or not found.", "Error", JOptionPane.ERROR_MESSAGE);
    }
} catch (SQLException ex) {
    ex.printStackTrace();
    JOptionPane.showMessageDialog(exitFrame, "Error occurred while updating status: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
}


                                                                        // Display ticket information
                                    String ticketInfo = "Ticket Number: " + ticketNumber +
                                    "\nVehicle Number: " + vehicleNo +
                                    "\nArrival Time: " + arrivalTime.toLocalDateTime() +
                                    "\nDeparture Time: " + departureLocalDateTime +
                                    "\nNumber of Hours Parked: " + durationHours +
                                    "\nAmount: $" + totalParkingFee;
                                    JOptionPane.showMessageDialog(exitFrame, ticketInfo, "Ticket Information", JOptionPane.INFORMATION_MESSAGE);

                                    } else {
                                    JOptionPane.showMessageDialog(exitFrame, "Parking fee not found for vehicle type.", "Error", JOptionPane.ERROR_MESSAGE);
                                }
                            } else {
                                JOptionPane.showMessageDialog(exitFrame, "Vehicle type not found for the given ticket.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(exitFrame, "Ticket information is incomplete.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(exitFrame, "Ticket number not found.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(exitFrame, "Error occurred while updating ticket information: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });*/

        JButton checkTicketButton = new JButton("CHECK TICKET");
        checkTicketButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        checkTicketButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String ticketNumber = ticketNumberField.getText();
                // Here you can validate the ticket number and fetch vehicle details
                try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                    // Query to retrieve arrival time
                    String ticketQuery = "SELECT ARRIVAL_TIME, VEHICLE_NO FROM ticket WHERE TICKET_NO = ?";
                    PreparedStatement ticketStmt = connection.prepareStatement(ticketQuery);
                    ticketStmt.setString(1, ticketNumber);
                    ResultSet ticketResult = ticketStmt.executeQuery();

                    if (ticketResult.next()) {
                        Timestamp arrivalTime = ticketResult.getTimestamp("ARRIVAL_TIME");
                        String vehicleNo = ticketResult.getString("VEHICLE_NO");

                        if (arrivalTime != null && vehicleNo != null) {
                            // Get the current time as the departure time
                            LocalDateTime departureLocalDateTime = LocalDateTime.now();

                            // Calculate duration in hours
                            long durationHours = Duration.between(arrivalTime.toLocalDateTime(), departureLocalDateTime).toHours();


                            // Query to retrieve vehicle type
                            String vehicleTypeQuery = "SELECT VEHICLE_TYPE FROM vehicle WHERE VEHICLE_NO = ?";
                            PreparedStatement vehicleTypeStmt = connection.prepareStatement(vehicleTypeQuery);
                            vehicleTypeStmt.setString(1, vehicleNo);
                            ResultSet vehicleTypeResult = vehicleTypeStmt.executeQuery();

                            if (vehicleTypeResult.next()) {
                                String vehicleType = vehicleTypeResult.getString("VEHICLE_TYPE");

                                // Query to retrieve parking fee per hour from the 'fair' table based on vehicle type
                                String feeQuery = "SELECT AMOUNT FROM fair WHERE VEHICLE_TYPE = ?";
                                PreparedStatement feeStmt = connection.prepareStatement(feeQuery);
                                feeStmt.setString(1, vehicleType);
                                ResultSet feeResult = feeStmt.executeQuery();

                                if (feeResult.next()) {
                                    double parkingFeePerHour = feeResult.getDouble("AMOUNT");

                                    // Calculate total parking fee
                                    double totalParkingFee;
                                    if (durationHours < 1) {
                                        // Set default fee to $20 for parking less than 1 hour
                                        totalParkingFee = 20.0;
                                    } else {
                                        // Calculate fee based on duration and vehicle type
                                        totalParkingFee = durationHours * parkingFeePerHour;
                                    }

                                    // Update the ticket with the departure time, duration hours, and amount
                                    String updateQuery = "UPDATE ticket SET DEPARTURE_TIME = ?, DURATION_HOURS = ?, AMOUNT = ? WHERE TICKET_NO = ?";
                                    PreparedStatement updateStmt = connection.prepareStatement(updateQuery);
                                    updateStmt.setTimestamp(1, Timestamp.valueOf(departureLocalDateTime));
                                    updateStmt.setLong(2, durationHours);
                                    updateStmt.setDouble(3, totalParkingFee);
                                    updateStmt.setString(4, ticketNumber);
                                    updateStmt.executeUpdate();

                                    // Retrieve the garage ID, floor ID, and lot ID from the ticket table
                                    String garageQuery = "SELECT GARAGE_ID, FLOOR_ID, LOT_ID FROM ticket WHERE TICKET_NO = ?";
                                    PreparedStatement garageStmt = connection.prepareStatement(garageQuery);
                                    garageStmt.setString(1, ticketNumber);
                                    ResultSet garageResult = garageStmt.executeQuery();

                                    if (garageResult.next()) {
                                        int garageId = garageResult.getInt("GARAGE_ID");
                                        int floorId = garageResult.getInt("FLOOR_ID");
                                        int lotId = garageResult.getInt("LOT_ID");

                                        // Update the status table for the respective parking spot to "free"
                                        String updateStatusQuery = "UPDATE status SET STATUS_AVAIL = 'free' WHERE GARAGE_ID = ? AND FLOOR_ID = ? AND LOT = ?";
                                        PreparedStatement updateStatusStmt = connection.prepareStatement(updateStatusQuery);
                                        updateStatusStmt.setInt(1, garageId);
                                        updateStatusStmt.setInt(2, floorId);
                                        updateStatusStmt.setInt(3, lotId);
                                        int rowsUpdated = updateStatusStmt.executeUpdate();

                                        if (rowsUpdated > 0) {
                                            System.out.println("Status updated for parking spot to 'free'");
                                        } else {
                                            System.out.println("No rows updated in status table");
                                        }
                                    } else {
                                        JOptionPane.showMessageDialog(exitFrame, "Ticket information is incomplete or not found.", "Error", JOptionPane.ERROR_MESSAGE);
                                    }

                                    // Display ticket information
                                    String ticketInfo = "Ticket Number: " + ticketNumber +
                                            "\nVehicle Number: " + vehicleNo +
                                            "\nArrival Time: " + arrivalTime.toLocalDateTime() +
                                            "\nDeparture Time: " + departureLocalDateTime +
                                            "\nNumber of Hours Parked: " + durationHours +
                                            "\nAmount: $" + totalParkingFee;
                                    JOptionPane.showMessageDialog(exitFrame, ticketInfo, "Ticket Information", JOptionPane.INFORMATION_MESSAGE);

                                } else {
                                    JOptionPane.showMessageDialog(exitFrame, "Parking fee not found for vehicle type.", "Error", JOptionPane.ERROR_MESSAGE);
                                }
                            } else {
                                JOptionPane.showMessageDialog(exitFrame, "Vehicle type not found for the given ticket.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(exitFrame, "Ticket information is incomplete.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(exitFrame, "Ticket number not found.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(exitFrame, "Error occurred while updating ticket information: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        exitPanel.add(checkTicketButton);
    
        contentPane.add(exitPanel, gbc);
    
        exitFrame.setContentPane(contentPane);
        exitFrame.setVisible(true);
    }
    
    
    // Helper function to create a rounded border
    private static Border createRoundedBorder() {
        int radius = 15;
        return BorderFactory.createEmptyBorder(radius, radius, radius, radius);
    }

    // BackgroundPanel class for displaying background image
    static class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel(String imagePath) {
            try {
                File imageFile = new File(imagePath);
                if (!imageFile.exists()) {
                    throw new IOException("Image file not found: " + imagePath);
                }
                backgroundImage = ImageIO.read(imageFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(800, 600);
        }
    }
    }
