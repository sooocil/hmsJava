package hospital.management.system;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import java.awt.*;

class DepartmentPanel extends JPanel {
    public DepartmentPanel() {
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

        JLabel title = new JLabel("Department");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(17, 24, 39));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        contentCard.add(title, gbc);

        JLabel deptLabel = new JLabel("Select Department:");
        deptLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        gbc.gridy = 1; gbc.gridwidth = 1;
        contentCard.add(deptLabel, gbc);

        JComboBox<String> deptBox = new JComboBox<>(new String[]{"Cardiology", "Neurology", "Orthopedics"});
        deptBox.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        gbc.gridx = 1;
        contentCard.add(deptBox, gbc);

        JButton viewButton = new JButton("View Details");
        viewButton.setBackground(new Color(59, 130, 246));
        viewButton.setForeground(Color.WHITE);
        viewButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        viewButton.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        viewButton.setFocusPainted(false);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        contentCard.add(viewButton, gbc);

        add(contentCard, BorderLayout.CENTER);
        setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
    }
}