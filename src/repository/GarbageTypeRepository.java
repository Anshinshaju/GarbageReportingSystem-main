package repository;

import model.GarbageType;
import model.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GarbageTypeRepository {
    public List<GarbageType> getAllGarbageTypes() {
        String sql = "SELECT * FROM garbage_types ORDER BY type_name";
        List<GarbageType> types = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                GarbageType type = new GarbageType(
                    rs.getInt("type_id"),
                    rs.getString("type_name")
                );
                types.add(type);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return types;
    }
}