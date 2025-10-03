package repository;

import model.Admin;
import model.DatabaseConnection;
import java.sql.*;

public class AdminRepository {
    public Admin getAdminByUsername(String username) {
        String sql = "SELECT * FROM admin WHERE username = ?";
        Admin admin = null;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                admin = new Admin(
                    rs.getString("admin_id"),
                    rs.getString("username"),
                    rs.getString("password")
                );
            }
        } catch (SQLException e) {
            // Create default admin if table doesn't exist
            createAdminTable();
            addDefaultAdmin();
            return getAdminByUsername(username); // Try again
        }
        return admin;
    }
    
    private void createAdminTable() {
        String sql = "CREATE TABLE IF NOT EXISTS admin (" +
                     "admin_id VARCHAR(10) PRIMARY KEY, " +
                     "username VARCHAR(100) UNIQUE NOT NULL, " +
                     "password VARCHAR(100) NOT NULL)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void addDefaultAdmin() {
        String sql = "INSERT INTO admin (admin_id, username, password) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "ADM001");
            pstmt.setString(2, "admin");
            pstmt.setString(3, "admin123");
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}