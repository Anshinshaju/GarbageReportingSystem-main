package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:sqlite:database/garbage_reporting.db";
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }
    
    public static void initializeDatabase() {
        try (Connection conn = getConnection()) {
            // Create users table
            conn.createStatement().executeUpdate(
                "CREATE TABLE IF NOT EXISTS users (" +
                "user_id VARCHAR(10) PRIMARY KEY, " +
                "name VARCHAR(100) NOT NULL, " +
                "email VARCHAR(100) UNIQUE NOT NULL, " +
                "password VARCHAR(100) NOT NULL, " +
                "address TEXT NOT NULL)"
            );
            
            // Create garbage_types table
            conn.createStatement().executeUpdate(
                "CREATE TABLE IF NOT EXISTS garbage_types (" +
                "type_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "type_name VARCHAR(100) NOT NULL UNIQUE)"
            );
            
            // Create reports table
            conn.createStatement().executeUpdate(
                "CREATE TABLE IF NOT EXISTS reports (" +
                "report_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id VARCHAR(10) NOT NULL, " +
                "waste_address TEXT NOT NULL, " +
                "type_id INTEGER NOT NULL, " +
                "description TEXT, " +
                "status VARCHAR(20) DEFAULT 'pending', " +
                "report_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY (user_id) REFERENCES users(user_id), " +
                "FOREIGN KEY (type_id) REFERENCES garbage_types(type_id))"
            );
            
            // Insert default garbage types if not exists
            conn.createStatement().executeUpdate(
                "INSERT OR IGNORE INTO garbage_types (type_name) VALUES " +
                "('Plastic Waste'), " +
                "('Organic Waste'), " +
                "('Electronic Waste'), " +
                "('Construction Waste'), " +
                "('General Waste')"
            );
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}