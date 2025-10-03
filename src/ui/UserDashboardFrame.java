package ui;

import model.User;
import model.Report;
import service.ReportService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class UserDashboardFrame extends JFrame {
    private User user;
    private ReportService reportService;
    
    private JTable reportsTable;
    private DefaultTableModel tableModel;
    private JButton btnMyReports, btnNewReport, btnLogout;
    private JPanel mainPanel;
    private CardLayout cardLayout;
    
    public UserDashboardFrame(User user) {
        this.user = user;
        reportService = new ReportService();
        
        setTitle("Garbage Reporting System - User Dashboard");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Main panel with CardLayout for switching views
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        // Create different views
        createWelcomeView();
        createMyReportsView();
        
        add(mainPanel);
        
        // Show welcome view first
        cardLayout.show(mainPanel, "welcome");
    }
    
    private void createWelcomeView() {
        JPanel welcomePanel = new JPanel(new BorderLayout());
        welcomePanel.setBackground(new Color(240, 248, 255));
        
        // Welcome message
        JLabel welcomeLabel = new JLabel("Welcome, " + user.getName() + "!", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(50, 0, 50, 0));
        welcomePanel.add(welcomeLabel, BorderLayout.NORTH);
        
        // Options panel
        JPanel optionsPanel = new JPanel(new GridLayout(2, 1, 0, 20));
        optionsPanel.setBackground(new Color(240, 248, 255));
        optionsPanel.setBorder(BorderFactory.createEmptyBorder(0, 150, 100, 150));
        
        btnMyReports = new JButton("My Reports");
        btnMyReports.setFont(new Font("Arial", Font.PLAIN, 18));
        btnMyReports.setBackground(new Color(70, 130, 180));
        btnMyReports.setForeground(Color.WHITE);
        
        btnNewReport = new JButton("New Report");
        btnNewReport.setFont(new Font("Arial", Font.PLAIN, 18));
        btnNewReport.setBackground(new Color(34, 139, 34));
        btnNewReport.setForeground(Color.WHITE);
        
        optionsPanel.add(btnMyReports);
        optionsPanel.add(btnNewReport);
        
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
        btnMyReports.addActionListener(e -> {
            loadUserReports();
            cardLayout.show(mainPanel, "myReports");
        });
        
        btnNewReport.addActionListener(e -> {
            openNewReportFrame();
        });
        
        btnLogout.addActionListener(e -> logout());
    }
    
    private void createMyReportsView() {
        JPanel reportsPanel = new JPanel(new BorderLayout());
        
        // Title and back button
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("My Reports", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        
        JButton btnBack = new JButton("Back");
        btnBack.addActionListener(e -> cardLayout.show(mainPanel, "welcome"));
        
        topPanel.add(btnBack, BorderLayout.WEST);
        topPanel.add(titleLabel, BorderLayout.CENTER);
        reportsPanel.add(topPanel, BorderLayout.NORTH);
        
        // Reports table
        String[] columnNames = {"Report ID", "Waste Address", "Waste Type", "Description", "Status", "Date"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        reportsTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(reportsTable);
        reportsPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Refresh and delete buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton btnRefresh = new JButton("Refresh");
        JButton btnDelete = new JButton("Delete Selected");
        
        btnRefresh.addActionListener(e -> loadUserReports());
        btnDelete.addActionListener(e -> deleteSelectedReport());
        
        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnDelete);
        reportsPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        mainPanel.add(reportsPanel, "myReports");
    }
    
    private void openNewReportFrame() {
        ReportFrame reportFrame = new ReportFrame(this, user);
        reportFrame.setVisible(true);
    }
    
    private void loadUserReports() {
        if (tableModel != null) {
            tableModel.setRowCount(0);
            List<Report> reports = reportService.getUserReports(user.getUserId());
            
            for (Report report : reports) {
                Object[] row = {
                    report.getReportId(),
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
    
    private void deleteSelectedReport() {
        int selectedRow = reportsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a report to delete", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int reportId = (Integer) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete report #" + reportId + "?", 
            "Confirm Delete", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (reportService.deleteReport(reportId)) {
                JOptionPane.showMessageDialog(this, "Report deleted successfully");
                loadUserReports();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete report", "Error", JOptionPane.ERROR_MESSAGE);
            }
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
    
    public void refreshReports() {
        loadUserReports();
        cardLayout.show(mainPanel, "myReports");
    }
}