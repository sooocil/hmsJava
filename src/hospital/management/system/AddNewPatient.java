package hospital.management.system;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AddNewPatient extends JPanel implements ActionListener {
    JComboBox<String> comboGender;
    JComboBox<String> comboRoom;
    JTextField textName, textAge, textContact, textCitizen, textAddress, textCondition, textDeposit;
    JLabel labelDate, PatientRandomID, roomPriceLabel, roomStatusLabel;
    Number PatientID;
    JButton buttonSubmit, buttonRefreshDate;

    private static final DateTimeFormatter DT_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public AddNewPatient() {
        /* Panel look */
        setBackground(new Color(247, 247, 250));
        setLayout(null);

        JPanel card = new JPanel(null);
        card.setBackground(Color.WHITE);
        card.setBounds(50, 50, 600, 600);
        card.setBorder(new CompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        JLabel title = new JLabel("Add New Patient");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(17, 24, 39));
        title.setBounds(20, 10, 300, 30);
        card.add(title);

        PatientID = getNextPatientID();
        PatientRandomID = new JLabel("Patient ID : " + PatientID);
        PatientRandomID.setFont(new Font("Segoe UI", Font.BOLD, 15));
        PatientRandomID.setBackground(new Color(120, 120, 120));
        PatientRandomID.setForeground(new Color(120, 120, 120));
        PatientRandomID.setBounds(335, 10, 300, 30);
        card.add(PatientRandomID);

        int y = 60;
        textName = addLabelAndField(card, "Full Name:", y); y += 40;
        comboGender = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        addLabelAndComponent(card, "Gender:", comboGender, y); y += 40;
        textAge = addLabelAndField(card, "Age:", y); y += 40;
        textContact = addLabelAndField(card, "Contact:", y); y += 40;
        textCitizen = addLabelAndField(card, "Citizenship No.:", y); y += 40;

        // Room selection with dynamic data
        JLabel roomLabel = new JLabel("Room No.:");
        roomLabel.setBounds(20, y, 120, 25);
        card.add(roomLabel);

        comboRoom = new JComboBox<>();
        comboRoom.setBounds(150, y, 180, 25);
        card.add(comboRoom);

        // Availability status label
        roomStatusLabel = new JLabel("");
        roomStatusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        roomStatusLabel.setBounds(340, y, 90, 25);
        card.add(roomStatusLabel);

        // Price label
        JLabel priceLabel = new JLabel("Price:");
        priceLabel.setBounds(20, y + 30, 120, 25);
        card.add(priceLabel);

        roomPriceLabel = new JLabel("");
        roomPriceLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        roomPriceLabel.setBounds(150, y + 30, 280, 25);
        card.add(roomPriceLabel);

        // Populate rooms and set initial price/status
        populateRoomCombo();
        comboRoom.addActionListener(e -> updateRoomDetails());

        y += 70;
        textAddress = addLabelAndField(card, "Address:", y); y += 40;
        textCondition = addLabelAndField(card, "Disease/Condition:", y); y += 40;
        textDeposit = addLabelAndField(card, "Deposit:", y); y += 45;

        JLabel lblDate = new JLabel("Date & Time:");
        lblDate.setBounds(20, y, 120, 25);
        card.add(lblDate);

        labelDate = new JLabel(now());
        labelDate.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        labelDate.setBounds(150, y, 180, 25);
        card.add(labelDate);

        buttonRefreshDate = new JButton("Refresh");
        buttonRefreshDate.setFocusPainted(false);
        buttonRefreshDate.setBounds(340, y, 90, 25);
        buttonRefreshDate.addActionListener(e -> labelDate.setText(now()));
        card.add(buttonRefreshDate);

        y += 40;
        buttonSubmit = new JButton("Add");
        buttonSubmit.setBackground(new Color(59, 130, 246));
        buttonSubmit.setForeground(Color.WHITE);
        buttonSubmit.setFont(new Font("Segoe UI", Font.BOLD, 14));
        buttonSubmit.setFocusPainted(false);
        buttonSubmit.setBounds(200, y, 120, 35);
        buttonSubmit.addActionListener(e -> labelDate.setText(now()));
        buttonSubmit.addActionListener(this);
        card.add(buttonSubmit);

        add(card);
    }

    private void populateRoomCombo() {
        try {
            conn c = new conn();
            Statement stmt = c.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("SELECT room_no FROM room WHERE availability = 'Available'");
            comboRoom.removeAllItems();
            boolean hasRooms = false;
            while (rs.next()) {
                comboRoom.addItem(rs.getString("room_no"));
                hasRooms = true;
            }
            if (!hasRooms) {
                comboRoom.addItem("No Room Available");
            }
            rs.close();
            stmt.close();
            c.getConnection().close();
            updateRoomDetails(); // Set initial price and status
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    private void updateRoomDetails() {
        String selectedRoom = (String) comboRoom.getSelectedItem();
        if (selectedRoom == null || selectedRoom.equals("No Room Available")) {
            roomPriceLabel.setText("");
            roomStatusLabel.setText("");
            return;
        }
        try {
            conn c = new conn();
            Statement stmt = c.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("SELECT price, availability FROM room WHERE room_no = '" + selectedRoom + "'");
            if (rs.next()) {
                roomPriceLabel.setText(rs.getString("price"));
                String availability = rs.getString("availability");
                roomStatusLabel.setText(availability);
                if (availability.equals("Available")) {
                    roomStatusLabel.setForeground(new Color(0, 128, 0)); // Green
                } else {
                    roomStatusLabel.setForeground(Color.RED);
                }
            } else {
                roomPriceLabel.setText("");
                roomStatusLabel.setText("");
            }
            rs.close();
            stmt.close();
            c.getConnection().close();
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    private Number getNextPatientID() {
        try {
            conn c = new conn();
            Statement stmt = c.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM patient_info");
            rs.next();
            int count = rs.getInt(1);
            rs.close();
            stmt.close();
            c.getConnection().close();
            return count + 1;
        } catch (Exception e) {
            System.out.println("Error: " + e);
            return 1;
        }
    }

    private String now() {
        return LocalDateTime.now().format(DT_FMT);
    }

    public void handleSubmit(ActionEvent e) {
        if (e.getSource() == buttonSubmit) {
            try {
                labelDate.setText(now());
                System.out.println("Now date: " + labelDate.getText());
            } catch (Exception ex) {
                System.out.println("Error: " + ex);
            }
        }
    }

    private JTextField addLabelAndField(JPanel panel, String labelText, int y) {
        JLabel label = new JLabel(labelText);
        label.setBounds(20, y, 120, 25);
        panel.add(label);

        JTextField textField = new JTextField();
        textField.setBounds(150, y, 280, 25);
        panel.add(textField);
        return textField;
    }

    private void addLabelAndComponent(JPanel panel, String labelText, JComponent component, int y) {
        JLabel label = new JLabel(labelText);
        label.setBounds(20, y, 120, 25);
        panel.add(label);

        component.setBounds(150, y, 280, 25);
        panel.add(component);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == buttonSubmit) {
            try {
                // Create connection
                conn c = new conn();
                Statement stmt = c.getConnection().createStatement();

                // Get all field values
                String name = textName.getText();
                String gender = comboGender.getSelectedItem().toString();
                String age = textAge.getText();
                String contact = textContact.getText();
                String citizenNo = textCitizen.getText();
                String roomNo = comboRoom.getSelectedItem().toString();
                String address = textAddress.getText();
                String condition = textCondition.getText();
                String deposit = textDeposit.getText();
                String dateTime = labelDate.getText();

                // Check if any required field is empty
                if (name.equals("") || gender.equals("") || age.equals("") ||
                        contact.equals("") || roomNo.equals("") || roomNo.equals("No Room Available") ||
                        address.equals("") || condition.equals("") || deposit.equals("")) {
                    JOptionPane.showMessageDialog(null, "Please fill all required fields!");
                    stmt.close();
                    c.getConnection().close();
                    return;
                }
                if (contact.length() < 10) {
                    JOptionPane.showMessageDialog(null, "Contact Number should be valid!");
                    stmt.close();
                    c.getConnection().close();
                    return;
                }

                // Check if room is available
                ResultSet rs = stmt.executeQuery("SELECT availability FROM room WHERE room_no = '" + roomNo + "'");
                if (rs.next()) {
                    String availability = rs.getString("availability");
                    if (!availability.equals("Available")) {
                        JOptionPane.showMessageDialog(null, "Selected room is not available!");
                        rs.close();
                        stmt.close();
                        c.getConnection().close();
                        return;
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Room not found!");
                    rs.close();
                    stmt.close();
                    c.getConnection().close();
                    return;
                }
                rs.close();

                // Create SQL query for patient_info
                String query = "INSERT INTO patient_info (name, gender, age, contact, citizen_no, room_no, address, disease_condition, deposit, time_date) VALUES ('" +
                        name + "', '" + gender + "', '" + age + "', '" +
                        contact + "', '" + citizenNo + "', '" + roomNo + "', '" +
                        address + "', '" + condition + "', '" + deposit + "', '" +
                        dateTime + "')";

                // Execute patient insertion
                stmt.executeUpdate(query);

                // Update room availability to Occupied
                String updateRoomQuery = "UPDATE room SET availability = 'Occupied' WHERE room_no = '" + roomNo + "'";
                stmt.executeUpdate(updateRoomQuery);

                JOptionPane.showMessageDialog(null, "Patient Added Successfully!");

                // Clear all fields
                textName.setText("");
                comboGender.setSelectedIndex(0);
                textAge.setText("");
                textContact.setText("");
                textCitizen.setText("");
                comboRoom.setSelectedIndex(0);
                textAddress.setText("");
                textCondition.setText("");
                textDeposit.setText("");
                labelDate.setText(now());

                // Update patient ID
                PatientID = getNextPatientID();
                PatientRandomID.setText("Patient ID : " + PatientID);

                // Update room combo
                populateRoomCombo();

                // Close statement and connection
                stmt.close();
                c.getConnection().close();

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
            }
        }
    }
}