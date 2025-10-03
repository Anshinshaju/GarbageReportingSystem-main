package ui;

import model.User;
import model.Report;
import service.ReportService;
import service.GarbageTypeService;
import model.GarbageType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ReportFrame extends JFrame {
    private User user;
    private UserDashboardFrame dashboard;
    private ReportService reportService;
    private GarbageTypeService garbageTypeService;
    
    private JTextField txtWasteAddress;
    private JComboBox<GarbageType> cmbGarbageType;
    private JTextArea txtDescription;
    private JButton btnSubmit, btnCancel;
    
    public ReportFrame(UserDashboardFrame dashboard, User user) {
        this.dashboard = dashboard;
        this.user = user;
        reportService = new ReportService();
        garbageTypeService = new GarbageTypeService();
        
        setTitle("Report New Waste");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(dashboard);
        
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Waste address field
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Waste Location Address:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 0;
        gbc.weightx = 1.0;
        txtWasteAddress = new JTextField(20);
        panel.add(txtWasteAddress, gbc);
        
        // Garbage type dropdown
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Waste Type:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 1;
        cmbGarbageType = new JComboBox<>();
        loadGarbageTypes();
        panel.add(cmbGarbageType, gbc);
        
        // Description field
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Description:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 2;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        txtDescription = new JTextArea(5, 20);
        JScrollPane scrollPane = new JScrollPane(txtDescription);
        panel.add(scrollPane, gbc);
        
        // Buttons
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JPanel buttonPanel = new JPanel(new FlowLayout());
        btnSubmit = new JButton("Submit Report");
        btnCancel = new JButton("Cancel");
        buttonPanel.add(btnSubmit);
        buttonPanel.add(btnCancel);
        panel.add(buttonPanel, gbc);
        
        add(panel);
        
        // Event listeners
        btnSubmit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitReport();
            }
        });
        
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
    
    private void loadGarbageTypes() {
        List<GarbageType> types = garbageTypeService.getAllGarbageTypes();
        for (GarbageType type : types) {
            cmbGarbageType.addItem(type);
        }
    }
    
    private void submitReport() {
        String wasteAddress = txtWasteAddress.getText().trim();
        GarbageType selectedType = (GarbageType) cmbGarbageType.getSelectedItem();
        String description = txtDescription.getText().trim();
        
        if (wasteAddress.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter the waste location address", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (selectedType == null) {
            JOptionPane.showMessageDialog(this, "Please select a waste type", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Create report object
        Report report = new Report(0, user.getUserId(), wasteAddress, 
                                 selectedType.getTypeId(), description, "pending", null);
        
        // Save report
        if (reportService.addReport(report)) {
            JOptionPane.showMessageDialog(this, "Report submitted successfully!");
            dashboard.refreshReports();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to submit report", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}