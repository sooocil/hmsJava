package hospital.management.system;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class AddRoomPanel extends JPanel implements ActionListener {
    JTextField numberField, priceField;
    JComboBox<String> typeBox;
    JButton addButton;

    public AddRoomPanel() {
        setBackground(new Color(247, 247, 250));
        setLayout(new BorderLayout());
        JPanel contentCard = new JPanel(new GridBagLayout());
        contentCard.setBackground(Color.WHITE);
        contentCard.setBorder(new CompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel title = new JLabel("Add Room");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(17, 24, 39));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        contentCard.add(title, gbc);

        JLabel numberLabel = new JLabel("Room Number:");
        numberLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        gbc.gridy = 1; gbc.gridwidth = 1;
        contentCard.add(numberLabel, gbc);

        numberField = new JTextField(20);
        numberField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        gbc.gridx = 1;
        contentCard.add(numberField, gbc);

        JLabel typeLabel = new JLabel("Room Type:");
        typeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        gbc.gridx = 0; gbc.gridy = 2;
        contentCard.add(typeLabel, gbc);

        typeBox = new JComboBox<>(new String[]{"General", "ICU", "Private"});
        typeBox.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        gbc.gridx = 1;
        contentCard.add(typeBox, gbc);

        JLabel priceLabel = new JLabel("Price:");
        priceLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        gbc.gridx = 0; gbc.gridy = 3;
        contentCard.add(priceLabel, gbc);

        priceField = new JTextField(20);
        priceField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        gbc.gridx = 1;
        contentCard.add(priceField, gbc);

        addButton = new JButton("Add Room");
        addButton.setBackground(new Color(59, 130, 246));
        addButton.setForeground(Color.WHITE);
        addButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        addButton.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        addButton.setFocusPainted(false);
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        contentCard.add(addButton, gbc);

        addButton.addActionListener(this);

        add(contentCard, BorderLayout.CENTER);
        setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            try {
                // Create connection
                conn c = new conn();

                // Get field values
                String roomNo = numberField.getText();
                String roomType = typeBox.getSelectedItem().toString();
                String price = priceField.getText();
                String availability = "Available";

                // Check if fields are empty
                if (roomNo.equals("") || roomType.equals("") || price.equals("")) {
                    JOptionPane.showMessageDialog(null, "Please fill all fields!");
                    return;
                }

                // Check if price is a number
                try {
                    Double.parseDouble(price);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Price must be a number!");
                    return;
                }

                // Create SQL query
                String query = "INSERT INTO room (room_no, availability, price, room_type) VALUES ('" +
                        roomNo + "', '" + availability + "', '" + price + "', '" + roomType + "')";

                // Execute query
                Statement stmt = c.getConnection().createStatement();
                stmt.executeUpdate(query);

                JOptionPane.showMessageDialog(null, "Room Added Successfully!");

                // Clear fields
                numberField.setText("");
                typeBox.setSelectedIndex(0);
                priceField.setText("");

                // Close statement and connection
                stmt.close();
                c.getConnection().close();

            } catch (Exception ex) {
                System.out.println("Error: " + ex);
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
            }
        }
    }
}