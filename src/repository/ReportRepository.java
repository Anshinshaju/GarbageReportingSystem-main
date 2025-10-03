package repository;

import model.Report;
import model.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReportRepository {
    public boolean addReport(Report report) {
        String sql = "INSERT INTO reports(user_id, waste_address, type_id, description) VALUES(?,?,?,?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, report.getUserId());
            pstmt.setString(2, report.getWasteAddress());
            pstmt.setInt(3, report.getTypeId());
            pstmt.setString(4, report.getDescription());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Report> getReportsByUserId(String userId) {
        String sql = "SELECT r.*, g.type_name FROM reports r " +
                     "JOIN garbage_types g ON r.type_id = g.type_id " +
                     "WHERE r.user_id = ? ORDER BY r.report_date DESC";
        List<Report> reports = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Report report = new Report(
                    rs.getInt("report_id"),
                    rs.getString("user_id"),
                    rs.getString("waste_address"),
                    rs.getInt("type_id"),
                    rs.getString("description"),
                    rs.getString("status"),
                    rs.getTimestamp("report_date")
                );
                report.setTypeName(rs.getString("type_name"));
                reports.add(report);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reports;
    }
    
    public List<Report> getAllReports() {
        String sql = "SELECT r.*, g.type_name, u.name as user_name FROM reports r " +
                     "JOIN garbage_types g ON r.type_id = g.type_id " +
                     "JOIN users u ON r.user_id = u.user_id " +
                     "ORDER BY r.report_date DESC";
        List<Report> reports = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Report report = new Report(
                    rs.getInt("report_id"),
                    rs.getString("user_id"),
                    rs.getString("waste_address"),
                    rs.getInt("type_id"),
                    rs.getString("description"),
                    rs.getString("status"),
                    rs.getTimestamp("report_date")
                );
                report.setTypeName(rs.getString("type_name"));
                reports.add(report);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reports;
    }
    
    public boolean deleteReport(int reportId) {
        String sql = "DELETE FROM reports WHERE report_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, reportId);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean updateReportStatus(int reportId, String status) {
        String sql = "UPDATE reports SET status = ? WHERE report_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, reportId);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}