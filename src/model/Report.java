package model;

import java.sql.Timestamp;

public class Report {
    private int reportId;
    private String userId;
    private String wasteAddress;
    private int typeId;
    private String description;
    private String status;
    private Timestamp reportDate;
    private String typeName; // For display purposes
    
    public Report(int reportId, String userId, String wasteAddress, int typeId, 
                 String description, String status, Timestamp reportDate) {
        this.reportId = reportId;
        this.userId = userId;
        this.wasteAddress = wasteAddress;
        this.typeId = typeId;
        this.description = description;
        this.status = status;
        this.reportDate = reportDate;
    }
    
    // Getters and setters
    public int getReportId() { return reportId; }
    public void setReportId(int reportId) { this.reportId = reportId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getWasteAddress() { return wasteAddress; }
    public void setWasteAddress(String wasteAddress) { this.wasteAddress = wasteAddress; }
    public int getTypeId() { return typeId; }
    public void setTypeId(int typeId) { this.typeId = typeId; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Timestamp getReportDate() { return reportDate; }
    public void setReportDate(Timestamp reportDate) { this.reportDate = reportDate; }
    public String getTypeName() { return typeName; }
    public void setTypeName(String typeName) { this.typeName = typeName; }
}