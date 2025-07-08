package hospital.management.system;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import java.awt.*;

class DischargePatientPanel extends JPanel {
    public DischargePatientPanel() {
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

        JLabel title = new JLabel("Discharge Patient");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(17, 24, 39));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        contentCard.add(title, gbc);

        JLabel idLabel = new JLabel("Patient ID:");
        idLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        gbc.gridy = 1; gbc.gridwidth = 1;
        contentCard.add(idLabel, gbc);

        JTextField idField = new JTextField(20);
        idField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        gbc.gridx = 1;
        contentCard.add(idField, gbc);

        JButton dischargeButton = new JButton("Discharge");
        dischargeButton.setBackground(new Color(59, 130, 246));
        dischargeButton.setForeground(Color.WHITE);
        dischargeButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        dischargeButton.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        dischargeButton.setFocusPainted(false);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        contentCard.add(dischargeButton, gbc);

        add(contentCard, BorderLayout.CENTER);
        setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
    }
}