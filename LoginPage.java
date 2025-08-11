
package Jewel;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LoginPage {
    private JFrame frame;
    private JTextField emailField;
    private JPasswordField passwordField;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginPage::new);
    }

    public LoginPage() {
        frame = new JFrame("Login - Silver Treasure");
        frame.setSize(450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        // Main Panel with background color
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(245, 245, 245));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // Title
        JLabel title = new JLabel("Silver Treasure Login");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Serif", Font.BOLD, 22));
        title.setForeground(new Color(60, 63, 65));
        mainPanel.add(title);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Email Field
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
//        emailLabel.setBounds(40,50,50,20);
        emailLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        mainPanel.add(emailLabel);
        emailField = new JTextField(20);
        Dimension fieldSize = new Dimension(300, 30); // Increase height to 50px
        emailField.setPreferredSize(fieldSize);
        emailField.setMaximumSize(fieldSize);
        emailField.setMinimumSize(fieldSize);

        mainPanel.add(emailField);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Password Field
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
        JButton loginBtn = new JButton("Login");
        JButton registerBtn = new JButton("Sign Up");
        styleButton(loginBtn);
        styleButton(registerBtn);
        buttonPanel.add(loginBtn);
        buttonPanel.add(registerBtn);
        mainPanel.add(buttonPanel);

        // Add to frame
        frame.setContentPane(mainPanel);
        frame.setVisible(true);

        // Actions
        loginBtn.addActionListener(e -> login());
        registerBtn.addActionListener(e -> {
            frame.dispose();
            new RegisterPage(); // Open registration page
        });
    }

    private void login() {
        String email = emailField.getText();
        String password = String.valueOf(passwordField.getPassword());

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/jewelry_db", "root", "your_password");
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE email = ? AND password = ?")) {

            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(frame, "Login successful!");
                frame.dispose(); // Close login window
                // 🟢 Launch Jewelry App
                SwingUtilities.invokeLater(() -> new JewelryApp().createAndShowGUI());

            } else {
                JOptionPane.showMessageDialog(frame, "Invalid credentials.", "Error", JOptionPane.ERROR_MESSAGE);
                emailField.setText("");
                passwordField.setText("");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
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


