package hospital.management.system;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import java.awt.*;

class DashboardPanel extends JPanel {
    public DashboardPanel() {
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

        JLabel title = new JLabel("Dashboard");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(17, 24, 39));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        contentCard.add(title, gbc);

        JLabel patientsLabel = new JLabel("Total Patients: 0");
        patientsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        gbc.gridy = 1; gbc.gridwidth = 1;
        contentCard.add(patientsLabel, gbc);

        JLabel roomsLabel = new JLabel("Available Rooms: 0");
        roomsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        gbc.gridy = 2;
        contentCard.add(roomsLabel, gbc);

        add(contentCard, BorderLayout.CENTER);
        setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
    }
}