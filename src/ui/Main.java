package ui;

import model.DatabaseConnection;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {
    public static void main(String[] args) {
        // Initialize database
        DatabaseConnection.initializeDatabase();
        
        // Start the application
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowWelcomePage();
            }
        });
    }
    
    // FIXED: Changed from private to public
    public static void createAndShowWelcomePage() {
        JFrame welcomeFrame = new JFrame("Garbage Reporting System");
        welcomeFrame.setSize(500, 400);
        welcomeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        welcomeFrame.setLocationRelativeTo(null);
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 248, 255));
        
        // Title
        JLabel titleLabel = new JLabel("Garbage Reporting System", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0, 100, 0));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(40, 0, 40, 0));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        // Center panel with buttons
        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 0, 20));
        centerPanel.setBackground(new Color(240, 248, 255));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 100, 60, 100));
        
        JButton btnLogin = new JButton("Login");
        btnLogin.setFont(new Font("Arial", Font.PLAIN, 18));
        btnLogin.setBackground(new Color(70, 130, 180));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        
        JButton btnRegister = new JButton("Register");
        btnRegister.setFont(new Font("Arial", Font.PLAIN, 18));
        btnRegister.setBackground(new Color(34, 139, 34));
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setFocusPainted(false);
        
        centerPanel.add(btnLogin);
        centerPanel.add(btnRegister);
        
        panel.add(centerPanel, BorderLayout.CENTER);
        
        // Footer
        JLabel footerLabel = new JLabel("Report garbage issues efficiently", JLabel.CENTER);
        footerLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        footerLabel.setForeground(Color.GRAY);
        footerLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        panel.add(footerLabel, BorderLayout.SOUTH);
        
        welcomeFrame.add(panel);
        welcomeFrame.setVisible(true);
        
        // Event listeners
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                welcomeFrame.dispose();
                openLoginFrame();
            }
        });
        
        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                welcomeFrame.dispose();
                openRegistrationFrame();
            }
        });
    }
    
    private static void openLoginFrame() {
        LoginFrame loginFrame = new LoginFrame();
        loginFrame.setVisible(true);
    }
    
    private static void openRegistrationFrame() {
        // Create login frame first and pass it to registration frame
        LoginFrame loginFrame = new LoginFrame();
        RegistrationFrame registrationFrame = new RegistrationFrame(loginFrame);
        registrationFrame.setVisible(true);
    }
}