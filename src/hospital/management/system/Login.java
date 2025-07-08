package hospital.management.system;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

public class Login extends JFrame implements ActionListener {

    JTextField textField;
    JPasswordField jPasswordField;
    JButton loginButton;
    JButton cancelButton;

    int winWidth = 1080;
    int winHeight = 720;
    int CdivWidth = 480; // Slightly slimmer for elegance
    int CdivHeight = 420; // Slightly taller for balance

    Login() {
        setTitle("Hospital Management System - Login");
        setSize(winWidth, winHeight);
        setLocationRelativeTo(null); // Center on screen
        setLayout(null);
        getContentPane().setBackground(new Color(249, 250, 251)); // Soft off-white background

        // Center panel (Cdiv)
        JPanel Cdiv = new JPanel();
        Cdiv.setLayout(null);
        Cdiv.setBackground(Color.WHITE);
        Cdiv.setBounds((winWidth - CdivWidth) / 2, (winHeight - CdivHeight) / 2, CdivWidth, CdivHeight);
        Cdiv.setBorder(new EmptyBorder(30, 30, 30, 30)); // Increased padding
        Cdiv.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235), 1), // Subtle border
                new EmptyBorder(30, 30, 30, 30)
        ));
        Cdiv.setBackground(new Color(255, 255, 255)); // Pure white for card effect
        add(Cdiv);

        // Title
        JLabel title = new JLabel("Login to Your Account");
        title.setBounds(0, 20, CdivWidth, 40);
        title.setFont(new Font("Arial", Font.BOLD, 24)); // Modern font
        title.setForeground(new Color(17, 24, 39)); // Dark navy
        title.setHorizontalAlignment(SwingConstants.CENTER);
        Cdiv.add(title);

        int labelX = 30;
        int fieldX = 150;
        int labelW = 110;
        int fieldW = 280; // Wider fields for balance
        int rowGap = 60; // Increased for breathing room

        // Username label
        JLabel nameLabel = new JLabel("Username:");
        nameLabel.setBounds(labelX, 90, labelW, 30);
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 14)); // Clean, modern font
        nameLabel.setForeground(new Color(75, 85, 99)); // Soft gray
        Cdiv.add(nameLabel);

        // Username field
        textField = new JTextField();
        textField.setBounds(fieldX, 90, fieldW, 36); // Taller for elegance
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        textField.setBackground(new Color(249, 250, 251)); // Light gray background
        textField.setForeground(new Color(17, 24, 39)); // Dark text
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235), 1), // Subtle border
                BorderFactory.createEmptyBorder(8, 12, 8, 12) // Padding
        ));
        Cdiv.add(textField);

        // Password label
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(labelX, 90 + rowGap, labelW, 30);
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordLabel.setForeground(new Color(75, 85, 99));
        Cdiv.add(passwordLabel);

        // Password field
        jPasswordField = new JPasswordField();
        jPasswordField.setBounds(fieldX, 90 + rowGap, fieldW, 36);
        jPasswordField.setFont(new Font("Arial", Font.PLAIN, 14));
        jPasswordField.setBackground(new Color(249, 250, 251));
        jPasswordField.setForeground(new Color(17, 24, 39));
        jPasswordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        Cdiv.add(jPasswordField);

        // Role label
        JLabel roleLabel = new JLabel("Role: Receptionist");
        roleLabel.setBounds(fieldX, 90 + 2 * rowGap, fieldW, 25);
        roleLabel.setFont(new Font("Arial", Font.ITALIC, 12)); // Smaller, subtler
        roleLabel.setForeground(new Color(107, 114, 128)); // Lighter gray
        Cdiv.add(roleLabel);

        // Login button
        loginButton = new JButton("Login");
        loginButton.setBounds(150, 90 + 3 * rowGap, 140, 40); // Wider, taller
        loginButton.setBackground(new Color(59, 130, 246)); // Vibrant blue
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        loginButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(new Color(96, 165, 250)); // Lighter blue on hover
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(new Color(59, 130, 246));
            }
        });
        loginButton.addActionListener(this);
        Cdiv.add(loginButton);

        // Cancel button
        cancelButton = new JButton("Cancel");
        cancelButton.setBounds(300, 90 + 3 * rowGap, 140, 40);
        cancelButton.setBackground(new Color(243, 244, 246)); // Light gray
        cancelButton.setForeground(new Color(75, 85, 99)); // Dark gray text
        cancelButton.setFocusPainted(false);
        cancelButton.setFont(new Font("Arial", Font.BOLD, 14));
        cancelButton.setBorder(BorderFactory.createLineBorder(new Color(229, 231, 235), 1));
        cancelButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                cancelButton.setBackground(new Color(229, 231, 235)); // Slightly darker on hover
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                cancelButton.setBackground(new Color(243, 244, 246));
            }
        });
        cancelButton.addActionListener(this);
        Cdiv.add(cancelButton);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            try {
                conn c = new conn();
                String user = textField.getText();
                String pass = new String(jPasswordField.getPassword()); // Use getPassword() for security

                String q = "select * from login where ID = '" + user + "' and PW = '" + pass + "'";
                ResultSet resultSet = c.statement.executeQuery(q);

                if (resultSet.next()) {
                    new ReceptionPanel();
                    setVisible(false);
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid Credentials!");
                }
            } catch (Exception E) {
                E.printStackTrace();
            }
        } else {
            int code = 20;
            System.out.println("Program exited with code: " + code);
            System.exit(code);
        }
    }

    public static void main(String[] args) {
        new Login();
    }
}