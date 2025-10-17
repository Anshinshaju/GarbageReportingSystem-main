package ui;

import model.User;
import service.UserService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class RegistrationFrame extends JFrame {
    private JTextField txtName, txtEmail, txtAddress;
    private JPasswordField txtPassword, txtConfirmPassword;
    private JButton btnRegister, btnCancel;
    
    private UserService userService;
    private LoginFrame loginFrame;
    
    public RegistrationFrame(LoginFrame loginFrame) {
        this.loginFrame = loginFrame;
        userService = new UserService();
        
        setTitle("User Registration");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(loginFrame);
        
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Name field
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Full Name:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 0;
        txtName = new JTextField(20);
        panel.add(txtName, gbc);
        
        // Email field
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Email:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 1;
        txtEmail = new JTextField(20);
        panel.add(txtEmail, gbc);
        
        // Password field
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Password:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 2;
        txtPassword = new JPasswordField(20);
        panel.add(txtPassword, gbc);
        
        // Confirm Password field
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Confirm Password:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 3;
        txtConfirmPassword = new JPasswordField(20);
        panel.add(txtConfirmPassword, gbc);
        
        // Address field
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Address:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 4;
        txtAddress = new JTextField(20);
        panel.add(txtAddress, gbc);
        
        // Buttons
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel(new FlowLayout());
        btnRegister = new JButton("Register");
        btnCancel = new JButton("Cancel");
        buttonPanel.add(btnRegister);
        buttonPanel.add(btnCancel);
        panel.add(buttonPanel, gbc);
        
        add(panel);
        
        // Event listeners
        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });
        
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goBackToLogin();
            }
        });
    }
    
    private void registerUser() {
        String name = txtName.getText().trim();
        String email = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword());
        String confirmPassword = new String(txtConfirmPassword.getPassword());
        String address = txtAddress.getText().trim();
        
        // Validation
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || address.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (userService.isEmailExists(email)) {
            JOptionPane.showMessageDialog(this, "Email already registered", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Generate random user ID
        String userId = generateUserId();
        
        // Create user object
        User user = new User(userId, name, email, password, address);
        
        // Save user
        if (userService.registerUser(user)) {
            JOptionPane.showMessageDialog(this, 
                "Registration successful! Your User ID is: " + userId + "\nPlease use this ID to login.", 
                "Success", JOptionPane.INFORMATION_MESSAGE);
            goBackToLogin();
        } else {
            JOptionPane.showMessageDialog(this, "Registration failed", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void goBackToLogin() {
        dispose();
        if (loginFrame != null) {
            loginFrame.setVisible(true);
        } else {
            // If loginFrame is null, go back to welcome page
            Main.createAndShowWelcomePage();
        }
    }
    
    private String generateUserId() {
        Random random = new Random();
        return "USR" + (1000 + random.nextInt(9000));
    }
}