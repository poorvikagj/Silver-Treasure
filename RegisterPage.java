package Jewel;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class RegisterPage {
    private JFrame frame;
    private JTextField emailField;
    private JPasswordField passwordField;

    public RegisterPage() {
        frame = new JFrame("Sign Up - Silver Treasure");
        frame.setSize(450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        // Main Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(245, 245, 245));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // Title
        JLabel title = new JLabel("Create a New Account");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Serif", Font.BOLD, 22));
        title.setForeground(new Color(60, 63, 65));
        mainPanel.add(title);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        Dimension fieldSize = new Dimension(300, 30);
        // Email
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        emailLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        mainPanel.add(emailLabel);
        emailField = new JTextField(20);
        emailField.setPreferredSize(fieldSize);
        emailField.setMaximumSize(fieldSize);
        emailField.setMinimumSize(fieldSize);
        mainPanel.add(emailField);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Password
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        passwordLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        mainPanel.add(passwordLabel);
        passwordField = new JPasswordField(20);
        passwordField.setPreferredSize(fieldSize);
        passwordField.setMaximumSize(fieldSize);
        passwordField.setMinimumSize(fieldSize);
        mainPanel.add(passwordField);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(245, 245, 245));
        JButton registerBtn = new JButton("Register");
        JButton backBtn = new JButton("Back to Login");
        styleButton(registerBtn);
        styleButton(backBtn);
        buttonPanel.add(registerBtn);
        buttonPanel.add(backBtn);
        mainPanel.add(buttonPanel);

        frame.setContentPane(mainPanel);
        frame.setVisible(true);

        // Actions
        registerBtn.addActionListener(e -> register());
        backBtn.addActionListener(e -> {
            frame.dispose();
            new LoginPage();
        });
    }

    private void register() {
        String email = emailField.getText();
        String password = String.valueOf(passwordField.getPassword());

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/jewelry_db", "root", "Poorvika@21");
             PreparedStatement ps = conn.prepareStatement("INSERT INTO users (email, password) VALUES (?, ?)")) {
            ps.setString(1, email);
            ps.setString(2, password);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(frame, "Registration successful!");
            frame.dispose();
            new LoginPage();
        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate entry")) {
                JOptionPane.showMessageDialog(frame, "Email already exists.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                e.printStackTrace();
            }
        }
    }

    private void styleButton(JButton button) {
        button.setFocusPainted(false);
        button.setFont(new Font("SansSerif", Font.BOLD, 13));
        button.setBackground(new Color(100, 149, 237));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
}
