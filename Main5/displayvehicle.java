import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class displayvehicle extends JFrame implements ActionListener {
    private ParkingSystem parkingSystem;
    private JComboBox<String> floorComboBox;
    private JPanel floorPanel;
    private JTextField tokenNumberTextField;
    private int selectedFloorIndex = -1;
    private Map<Integer, String> tokenSlotMap = new HashMap<>();

    public displayvehicle() {
        super("Parking System");
        parkingSystem = new ParkingSystem();
        setLayout(new BorderLayout());

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));

        JPanel floorSelectionPanel = new JPanel();
        floorSelectionPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        floorSelectionPanel.add(new JLabel("Select Floor: "));
        floorComboBox = new JComboBox<>(new String[]{"1", "2 ", "3 ", "4 ", "5"});
        floorComboBox.addActionListener(this);
        floorSelectionPanel.add(floorComboBox);
        controlPanel.add(floorSelectionPanel);

        JPanel tokenNumberPanel = new JPanel();
        tokenNumberPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        tokenNumberPanel.add(new JLabel("Enter Token Number: "));
        tokenNumberTextField = new JTextField(10);
        tokenNumberPanel.add(tokenNumberTextField);
        controlPanel.add(tokenNumberPanel);

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

    private void setupFloorPanel(int[][] availableSlots, int tokenNumber) {
        floorPanel.removeAll();
        int selectedIndex = floorComboBox.getSelectedIndex();
        selectedFloorIndex = selectedIndex;
        String[] floorNames = {"1", "2", "3", "4", "5"};

        JPanel floor = new JPanel();
        floor.setBorder(BorderFactory.createTitledBorder(floorComboBox.getSelectedItem().toString()));
        int slotsCount = availableSlots[selectedIndex].length;
        int rows = (int) Math.ceil((double) slotsCount / 10);
        floor.setLayout(new GridLayout(rows, 10));

        ButtonGroup buttonGroup = new ButtonGroup();

        for (int j = 0; j < slotsCount; j++) {
            int slotNumber = j + 1;
            String slotName = floorNames[selectedIndex] + "-" + slotNumber;
            JToggleButton toggleButton = new JToggleButton(slotName);

            toggleButton.setBackground(new Color(34, 34, 34));
            toggleButton.setForeground(new Color(192, 192, 192));

            int token = parkingSystem.getTokenForSlot(selectedIndex, slotNumber);

            if (token != -1 && token == tokenNumber) {
                toggleButton.setSelected(true);
                toggleButton.setBackground(Color.YELLOW); // Change background color
            }

            floor.add(toggleButton);
            buttonGroup.add(toggleButton);
        }
        floorPanel.add(floor);
        revalidate();
    }

    public void actionPerformed(ActionEvent e) {
        if ("Display Slot".equals(e.getActionCommand())) {
            try {
                int tokenNumber = Integer.parseInt(tokenNumberTextField.getText());
                // Query the database to get floor ID and lot ID for the entered token number
                int floorID = parkingSystem.getFloorIDForToken(tokenNumber);
                int lotID = parkingSystem.getLotIDForToken(tokenNumber);
                if (floorID != -1 && lotID != -1) {
                    setupFloorPanel(parkingSystem.getAvailableSlots(), tokenNumber);
                    highlightAllocatedLot(floorID, lotID);
                } else {
                    JOptionPane.showMessageDialog(this, "No allocation found for the entered token number.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid token number.");
            }
        }
    }

    private void highlightAllocatedLot(int floorID, int lotID) {
        System.out.println("Highlighting floor " + floorID + ", lot " + lotID); // Print floor and lot IDs
        Component[] components = floorPanel.getComponents();
        for (Component component : components) {
            if (component instanceof JPanel) {
                JPanel floor = (JPanel) component;
                if (floor.getBorder().toString().contains(String.valueOf(floorID))) {
                    System.out.println("Found matching floor");
                    for (Component button : floor.getComponents()) {
                        if (button instanceof JToggleButton) {
                            JToggleButton toggleButton = (JToggleButton) button;
                            String buttonLabel = toggleButton.getText();
                            String[] parts = buttonLabel.split("-");
                            int buttonLotID = Integer.parseInt(parts[1]);
                            if (buttonLotID == lotID) {
                                System.out.println("Highlighting lot " + lotID);
                                toggleButton.setBackground(Color.YELLOW);
                                toggleButton.setSelected(true);
                            }
                        }
                    }
                    break;
                }
            }
        }
    }
    
    class ParkingSystem {
        private int[][] floors;
        private Map<Integer, String> tokenSlotMap;

        public ParkingSystem() {
            this.floors = new int[5][];
            floors[0] = new int[30];
            floors[1] = new int[50];
            floors[2] = new int[50];
            floors[3] = new int[100];
            floors[4] = new int[100];
            this.tokenSlotMap = new HashMap<>();
            initializeTokenSlotMap();
        }

        public int[][] getAvailableSlots() {
            return floors;
        }

        private void initializeTokenSlotMap() {
            tokenSlotMap.put(1001, "1");
            tokenSlotMap.put(1002, "25");
            tokenSlotMap.put(1003, "40");
            tokenSlotMap.put(1004, "7");
            tokenSlotMap.put(1005, "10");
        }

        public int getTokenForSlot(int floorIndex, int slotNumber) {
            String slotID = getSlotID(floorIndex, slotNumber);
            for (Map.Entry<Integer, String> entry : tokenSlotMap.entrySet()) {
                if (entry.getValue().equals(slotID)) {
                    return entry.getKey();
                }
            }
            return -1;
        }

        public int getFloorIDForToken(int tokenNumber) {
            int floorID = -1;
            try {
                Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "system", "123root");
                String sql = "SELECT FLOOR_ID FROM TICKET WHERE TICKET_NO = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, tokenNumber);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    floorID = rs.getInt("FLOOR_ID");
                }
                rs.close();
                pstmt.close();
                conn.close();
                System.out.println("Floor ID for token " + tokenNumber + ": " + floorID); // Print floor ID
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return floorID;
        }
        
        public int getLotIDForToken(int tokenNumber) {
            int lotID = -1;
            try {
                Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "system", "123root");
                String sql = "SELECT LOT_ID FROM TICKET WHERE TICKET_NO = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, tokenNumber);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    lotID = rs.getInt("LOT_ID");
                }
                rs.close();
                pstmt.close();
                conn.close();
                System.out.println("Lot ID for token " + tokenNumber + ": " + lotID); // Print lot ID
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return lotID;
        }
        
        private String getSlotID(int floorIndex, int slotNumber) {
            String[] floorNames = {"1", "2", "3", "4", "5"};
            return floorNames[floorIndex] + "-" + slotNumber;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(displayvehicle::new);
    }
}
