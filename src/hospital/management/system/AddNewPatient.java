package hospital.management.system;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Beginner‑friendly form to add a new patient.
 * Shows current date‑time and lets the user refresh it with a button.
 */
public class AddNewPatient extends JPanel {
    // ─── Global form components ──────────────────────────────────────────────
    JComboBox<String> comboGender;
    JComboBox<Number> comboRoom;


    JTextField textName, textAge, textContact, textCitizen, textRoom, textAddress, textCondition;
    JLabel   labelDate;
    JButton  buttonSubmit, buttonRefreshDate;

    private static final DateTimeFormatter DT_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public AddNewPatient() {
        /* ── panel look ── */
        setBackground(new Color(247, 247, 250));
        setLayout(new BorderLayout());

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(10, 10, 10, 10);
        g.anchor = GridBagConstraints.WEST;

        /* ── helper to add a label‑field row ── */
        int row = 0;
        g.gridx = 0; g.gridy = row; g.gridwidth = 2;
        JLabel title = new JLabel("Add New Patient");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(17, 24, 39));
        card.add(title, g);

        row++;
        addRow(card, g, row++, "Full Name:", textName   = new JTextField(20));
        addRow(card, g, row++, "Gender:",    comboGender = new JComboBox<>(new String[]{"Male","Female","Other"}));
        addRow(card, g, row++, "Age:",       textAge    = new JTextField(5));
        addRow(card, g, row++, "Contact:",   textContact = new JTextField(15));
        addRow(card, g, row++, "Citizenship No.:", textCitizen = new JTextField(15));
        addRow(card, g, row++, "Room No. :",    comboRoom = new JComboBox<>(new Number[]{100,203,200,205,300,120}));
        addRow(card, g, row++, "Address:",   textAddress = new JTextField(20));
        addRow(card, g, row++, "Disease/Condition:", textCondition = new JTextField(20));

        g.gridy = row; g.gridx = 0; g.gridwidth = 1;
        card.add(new JLabel("Date & Time:"), g);

        labelDate = new JLabel(now());
        labelDate.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        g.gridx = 1;
        card.add(labelDate, g);

        buttonRefreshDate = new JButton("Refresh");
        buttonRefreshDate.setFocusPainted(false);
        buttonRefreshDate.addActionListener(e -> labelDate.setText(now()));
        g.gridx = 2;
        card.add(buttonRefreshDate, g);

        /* ── submit button spanning the full width ── */
        buttonSubmit = new JButton("Submit");
        buttonSubmit.setBackground(new Color(59, 130, 246));
        buttonSubmit.setForeground(Color.WHITE);
        buttonSubmit.setFont(new Font("Segoe UI", Font.BOLD, 14));
        buttonSubmit.setFocusPainted(false);
        g.gridy = ++row; g.gridx = 0; g.gridwidth = 3; g.anchor = GridBagConstraints.CENTER;
        card.add(buttonSubmit, g);

        add(card, BorderLayout.CENTER);
        setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
    }

    /** Returns formatted current date‑time. */
    private String now() {
        return LocalDateTime.now().format(DT_FMT);
    }

    public void handleSubmit(ActionEvent e) {
        if (e.getSource() == buttonSubmit) {
            try {
                labelDate.setText(now());
                System.out.println("Now date : " + labelDate);
            } catch (Exception E) {
                E.printStackTrace();
            }
        } else {
            int code = 20;
            System.out.println("Program exited with code: " + code);
            System.exit(code);
        }
    }

    /** Utility to add a labelled field to the grid. */
    private void addRow(JPanel panel, GridBagConstraints g, int row, String labelText, JComponent field) {
        g.gridy = row; g.gridx = 0; g.gridwidth = 1;
        panel.add(new JLabel(labelText), g);
        g.gridx = 1; g.gridwidth = 2;
        panel.add(field, g);
    }
}
