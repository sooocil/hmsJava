package hospital.management.system;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class ReceptionPanel extends JFrame {

    JButton logoutButton;
    JPanel mainPanel;

    public ReceptionPanel() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(1500, 750));
        JPanel leftPanel = new JPanel();
        leftPanel.setPreferredSize(new Dimension(420, 200));
        leftPanel.setBackground(new Color(23, 37, 84));
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(new CompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(209, 213, 219)),
                BorderFactory.createEmptyBorder(25, 15, 20, 0)
        ));

        JLabel sidebarTitle = new JLabel("HMS – Hospital Management System");
        sidebarTitle.setForeground(Color.WHITE);
        sidebarTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        sidebarTitle.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(34, 34, 34)),
                BorderFactory.createEmptyBorder(0, 0, 8, 0)
        ));

        leftPanel.add(sidebarTitle);

        // Button names
        String[] buttonNames = {
                "Dashboard",
                "All Patient Info",
                "View Room",
                "Employee Info",
                "Ambulance Info"
        };

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(247, 247, 250));

        for (String name : buttonNames) {
            // Create button
            JButton button = new JButton(name);
            button.setMaximumSize(new Dimension(400, 42));
            button.setBackground(new Color(23, 37, 84));
            button.setForeground(new Color(209, 213, 219));
            button.setFont(new Font("Segoe UI", Font.BOLD, 15));
            button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
            button.setFocusPainted(false);
            button.setContentAreaFilled(true);
            button.setHorizontalAlignment(SwingConstants.LEFT);

            // Hover and click effects
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    if (!button.isSelected()) {
                        button.setBackground(new Color(51, 65, 81));
                        button.setForeground(Color.WHITE);
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    if (!button.isSelected()) {
                        button.setBackground(new Color(23, 37, 84));
                        button.setForeground(new Color(209, 213, 219));
                    }
                }
            });

            // Action to switch page
            button.addActionListener(e -> {
                for (Component comp : leftPanel.getComponents()) {
                    if (comp instanceof JButton) {
                        JButton btn = (JButton) comp;
                        btn.setSelected(false);
                        btn.setBackground(new Color(23, 37, 84));
                        btn.setForeground(new Color(209, 213, 219));
                    }
                }
                button.setSelected(true);
                button.setBackground(new Color(147, 197, 253));
                button.setForeground(Color.WHITE);

                // Clear mainPanel and add new panel
                mainPanel.removeAll();
                JPanel newPanel;
                switch (name) {
                    case "Dashboard":
                        newPanel = new DashboardPanel();
                        break;
                    case "Add New Patient":
                        newPanel = new AddNewPatient();
                        break;
                    case "All Patient Info":
                        newPanel = new AllPatientInfo();
                        break;

                    case "View Room":
                        newPanel = new ViewRoom();
                        break;
                    case "Add Room":
                        newPanel = new AddRoomPanel();
                        break;
                    case "Employee Info":
                        newPanel = new EmployeeInfo();
                        break;

                    case "Ambulance Info":
                        newPanel = createPlaceholderPanel("Ambulance Information Coming Soon ....");
                        break;
                    default:
                        newPanel = createPlaceholderPanel("Welcome to " + name);
                }
                mainPanel.add(newPanel, BorderLayout.CENTER);
                mainPanel.revalidate();
                mainPanel.repaint();
            });

            leftPanel.add(button);
            leftPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        }

        leftPanel.add(Box.createVerticalGlue());

        logoutButton = new JButton("Logout");
        logoutButton.setMaximumSize(new Dimension(400, 42));
        logoutButton.setBackground(new Color(59, 130, 246));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFont(new Font("Segoe UI", Font.BOLD, 15));
        logoutButton.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        logoutButton.setFocusPainted(false);
        logoutButton.setContentAreaFilled(true);
        logoutButton.setHorizontalAlignment(SwingConstants.LEFT);

        logoutButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                logoutButton.setBackground(new Color(96, 165, 250));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                logoutButton.setBackground(new Color(59, 130, 246));
            }
        });

        logoutButton.addActionListener(e -> {
            dispose();
            new Login();
        });

        leftPanel.add(logoutButton);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        add(leftPanel, BorderLayout.WEST);
        add(mainPanel, BorderLayout.CENTER);

        // Show Dashboard by default
        mainPanel.add(new DashboardPanel(), BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();

        pack();
        setTitle("Hospital Management System - Reception Panel");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
    }

    private JPanel createPlaceholderPanel(String text) {
        JPanel tabPanel = new JPanel(new BorderLayout());
        tabPanel.setBackground(new Color(247, 247, 250));
        JPanel contentCard = new JPanel(new BorderLayout());
        contentCard.setBackground(Color.WHITE);
        contentCard.setBorder(new CompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        JLabel contentLabel = new JLabel(text, SwingConstants.CENTER);
        contentLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        contentLabel.setForeground(new Color(17, 24, 39));
        contentCard.add(contentLabel, BorderLayout.CENTER);
        tabPanel.add(contentCard, BorderLayout.CENTER);
        tabPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        return tabPanel;
    }

    public static void main(String[] args) {
        new ReceptionPanel();
    }
}