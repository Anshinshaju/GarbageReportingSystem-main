package ui;

import model.User;
import model.Admin;
import service.UserService;
import service.AdminService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {
    private final JTextField txtUserId;
    private final JPasswordField txtPassword;
    private final JButton btnLogin;
    private final JButton btnBack;
    private final JComboBox<String> cmbUserType;
    
    private final UserService userService;
    private final AdminService adminService;
    
    public LoginFrame() {
        userService = new UserService();
        adminService = new AdminService();
        
        setTitle("Garbage Reporting System - Login");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(240, 248, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Title
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel lblTitle = new JLabel("Login to System", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setForeground(new Color(0, 100, 0));
        panel.add(lblTitle, gbc);
        
        gbc.gridwidth = 1;
        
        // User type selection
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Login as:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 1;
        cmbUserType = new JComboBox<>(new String[]{"User", "Admin"});
        panel.add(cmbUserType, gbc);
        
        // User ID field
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("User ID:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 2;
        txtUserId = new JTextField(15);
        panel.add(txtUserId, gbc);
        
        // Password field
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Password:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 3;
        txtPassword = new JPasswordField(15);
        panel.add(txtPassword, gbc);
        
        // Login button
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        btnLogin = new JButton("Login");
        btnLogin.setBackground(new Color(70, 130, 180));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        panel.add(btnLogin, gbc);
        
        // Back button
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.gridwidth = 2;
        btnBack = new JButton("Back to Welcome Page");
        btnBack.setBackground(new Color(169, 169, 169));
        btnBack.setForeground(Color.WHITE);
        btnBack.setFocusPainted(false);
        panel.add(btnBack, gbc);
        
        add(panel);
        
        // Event listeners
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });
        
        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goBackToWelcome();
            }
        });
        
        cmbUserType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateLoginFields();
            }
        });
        
        updateLoginFields();
    }
    
    private void updateLoginFields() {
        String userType = (String) cmbUserType.getSelectedItem();
        if ("Admin".equals(userType)) {
            txtUserId.setText("");
            txtUserId.setEditable(true);
            txtUserId.setToolTipText("Enter admin username");
        } else {
            txtUserId.setEditable(true);
            txtUserId.setToolTipText("Enter your user ID");
        }
    }
    
    private void login() {
        String userType = (String) cmbUserType.getSelectedItem();
        String userId = txtUserId.getText().trim();
        String password = new String(txtPassword.getPassword());
        
        if (userId.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if ("Admin".equals(userType)) {
            Admin admin = adminService.loginAdmin(userId, password);
            if (admin != null) {
                JOptionPane.showMessageDialog(this, "Admin login successful!");
                openAdminDashboard();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid admin credentials", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            User user = userService.loginUser(userId, password);
            if (user != null) {
                JOptionPane.showMessageDialog(this, "Login successful! Welcome " + user.getName());
                openUserDashboard(user);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid user ID or password", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void goBackToWelcome() {
        dispose();
        // FIXED: Now calling the public method
        Main.createAndShowWelcomePage();
    }
    
    private void openUserDashboard(User user) {
        UserDashboardFrame dashboard = new UserDashboardFrame(user);
        dashboard.setVisible(true);
    }
    
    private void openAdminDashboard() {
        AdminDashboardFrame dashboard = new AdminDashboardFrame();
        dashboard.setVisible(true);
    }
}