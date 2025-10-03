package ui;

import service.ReportService;
import service.UserService;
import model.Report;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdminDashboardFrame extends JFrame {
    private ReportService reportService;
    private UserService userService;
    
    private JTable reportsTable;
    private DefaultTableModel tableModel;
    private JButton btnPending, btnAll, btnDone, btnLogout;
    private JPanel mainPanel;
    private CardLayout cardLayout;
    
    public AdminDashboardFrame() {
        reportService = new ReportService();
        userService = new UserService();
        
        setTitle("Garbage Reporting System - Admin Dashboard");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Main panel with CardLayout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        createWelcomeView();
        createReportsView();
        
        add(mainPanel);
        
        // Show welcome view first
        cardLayout.show(mainPanel, "welcome");
    }
    
    private void createWelcomeView() {
        JPanel welcomePanel = new JPanel(new BorderLayout());
        welcomePanel.setBackground(new Color(240, 248, 255));
        
        // Welcome message
        JLabel welcomeLabel = new JLabel("Admin Dashboard", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(50, 0, 50, 0));
        welcomePanel.add(welcomeLabel, BorderLayout.NORTH);
        
        // Options panel
        JPanel optionsPanel = new JPanel(new GridLayout(3, 1, 0, 15));
        optionsPanel.setBackground(new Color(240, 248, 255));
        optionsPanel.setBorder(BorderFactory.createEmptyBorder(0, 200, 100, 200));
        
        btnPending = new JButton("View Pending Reports");
        btnPending.setFont(new Font("Arial", Font.PLAIN, 16));
        btnPending.setBackground(new Color(255, 165, 0));
        btnPending.setForeground(Color.WHITE);
        
        btnAll = new JButton("View All Reports");
        btnAll.setFont(new Font("Arial", Font.PLAIN, 16));
        btnAll.setBackground(new Color(70, 130, 180));
        btnAll.setForeground(Color.WHITE);
        
        btnDone = new JButton("View Done Reports");
        btnDone.setFont(new Font("Arial", Font.PLAIN, 16));
        btnDone.setBackground(new Color(34, 139, 34));
        btnDone.setForeground(Color.WHITE);
        
        optionsPanel.add(btnPending);
        optionsPanel.add(btnAll);
        optionsPanel.add(btnDone);
        
        welcomePanel.add(optionsPanel, BorderLayout.CENTER);
        
        // Logout button
        btnLogout = new JButton("Logout");
        btnLogout.setBackground(new Color(220, 20, 60));
        btnLogout.setForeground(Color.WHITE);
        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        logoutPanel.setBackground(new Color(240, 248, 255));
        logoutPanel.add(btnLogout);
        welcomePanel.add(logoutPanel, BorderLayout.SOUTH);
        
        mainPanel.add(welcomePanel, "welcome");
        
        // Event listeners
        btnPending.addActionListener(e -> {
            filterReportsByStatus("pending");
            cardLayout.show(mainPanel, "reports");
        });
        
        btnAll.addActionListener(e -> {
            filterReportsByStatus("All");
            cardLayout.show(mainPanel, "reports");
        });
        
        btnDone.addActionListener(e -> {
            filterReportsByStatus("done");
            cardLayout.show(mainPanel, "reports");
        });
        
        btnLogout.addActionListener(e -> logout());
    }
    
    private void createReportsView() {
        JPanel reportsPanel = new JPanel(new BorderLayout());
        
        // Top panel with back button and title
        JPanel topPanel = new JPanel(new BorderLayout());
        JButton btnBack = new JButton("Back to Dashboard");
        btnBack.addActionListener(e -> cardLayout.show(mainPanel, "welcome"));
        
        JLabel titleLabel = new JLabel("Reports Management", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        
        topPanel.add(btnBack, BorderLayout.WEST);
        topPanel.add(titleLabel, BorderLayout.CENTER);
        reportsPanel.add(topPanel, BorderLayout.NORTH);
        
        // Reports table
        String[] columnNames = {"Report ID", "User ID", "Waste Address", "Waste Type", "Description", "Status", "Date"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        reportsTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(reportsTable);
        reportsPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Action buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton btnRefresh = new JButton("Refresh");
        JButton btnMarkDone = new JButton("Mark as Done");
        JButton btnMarkPending = new JButton("Mark as Pending");
        
        btnRefresh.addActionListener(e -> filterReportsByStatus("All"));
        btnMarkDone.addActionListener(e -> updateReportStatus("done"));
        btnMarkPending.addActionListener(e -> updateReportStatus("pending"));
        
        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnMarkDone);
        buttonPanel.add(btnMarkPending);
        
        reportsPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        mainPanel.add(reportsPanel, "reports");
    }
    
    private void filterReportsByStatus(String status) {
        tableModel.setRowCount(0);
        List<Report> reports = reportService.getAllReports();
        
        for (Report report : reports) {
            if ("All".equals(status) || status.equals(report.getStatus())) {
                Object[] row = {
                    report.getReportId(),
                    report.getUserId(),
                    report.getWasteAddress(),
                    report.getTypeName(),
                    report.getDescription(),
                    report.getStatus(),
                    report.getReportDate()
                };
                tableModel.addRow(row);
            }
        }
    }
    
    private void updateReportStatus(String status) {
        int selectedRow = reportsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a report", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int reportId = (Integer) tableModel.getValueAt(selectedRow, 0);
        
        if (reportService.updateReportStatus(reportId, status)) {
            JOptionPane.showMessageDialog(this, "Report status updated successfully");
            filterReportsByStatus("All");
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update report status", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to logout?", "Confirm Logout", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            openWelcomePage();
        }
    }
    
    private void openWelcomePage() {
        // Create and show the welcome page directly
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
        
        // Event listeners for welcome page buttons
        btnLogin.addActionListener(e -> {
            welcomeFrame.dispose();
            new LoginFrame().setVisible(true);
        });
        
        btnRegister.addActionListener(e -> {
            welcomeFrame.dispose();
            new RegistrationFrame(null).setVisible(true);
        });
    }
}