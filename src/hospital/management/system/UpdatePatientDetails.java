package hospital.management.system;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class UpdatePatientDetails extends JPanel {
    JComboBox<String> comboPatientId;
    JComboBox<String> comboGender;
    JComboBox<Number> comboRoom;

    JTextField textName, textAge, textContact, textCitizen, textAddress, textCondition, textDeposit;
    JLabel   labelDate, deposit;
    JButton  buttonSubmit, buttonRefreshDate;

    private static final DateTimeFormatter DT_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public UpdatePatientDetails() {
        /* Panel look */
        setBackground(new Color(247, 247, 250));
        setLayout(null);

        JPanel card = new JPanel(null);
        card.setBackground(Color.WHITE);
        card.setBounds(50, 50, 600, 600);
        card.setBorder(new CompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        JLabel title = new JLabel("Update Patient Details");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(17, 24, 39));
        title.setBounds(20, 10, 300, 30);
        card.add(title);

        int y = 60;
        textName = addLabelAndField(card, "Full Name:", y); y += 40;
        comboGender = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        addLabelAndComponent(card, "Gender:", comboGender, y); y += 40;
        textAge = addLabelAndField(card, "Age:", y); y += 40;
        textContact = addLabelAndField(card, "Contact:", y); y += 40;
        textCitizen = addLabelAndField(card, "Citizenship No.:", y); y += 40;
        comboRoom = new JComboBox<>(new Number[]{100, 203, 200, 205, 300, 120});
        addLabelAndComponent(card, "Room No.:", comboRoom, y); y += 40;
        textAddress = addLabelAndField(card, "Address:", y); y += 40;
        textCondition = addLabelAndField(card, "Disease/Condition:", y); y += 40;
        textDeposit = addLabelAndField(card, "Deposit:", y);y+=45;

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
        buttonSubmit = new JButton("Update");
        buttonSubmit.setBackground(new Color(59, 130, 246));
        buttonSubmit.setForeground(Color.WHITE);
        buttonSubmit.setFont(new Font("Segoe UI", Font.BOLD, 14));
        buttonSubmit.setFocusPainted(false);
        buttonSubmit.setBounds(200, y, 120, 35);
        buttonSubmit.addActionListener(e->labelDate.setText(now()));
        card.add(buttonSubmit);

        add(card);
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
                ex.printStackTrace();
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

    /** Adds a label and generic component (e.g., JComboBox) to the panel. */
    private void addLabelAndComponent(JPanel panel, String labelText, JComponent component, int y) {
        JLabel label = new JLabel(labelText);
        label.setBounds(20, y, 120, 25);
        panel.add(label);

        component.setBounds(150, y, 280, 25);
        panel.add(component);
    }
}
