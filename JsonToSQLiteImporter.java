import org.json.JSONArray;
import org.json.JSONObject;
import java.nio.file.*;
import java.sql.*;

public class JsonToSQLiteImporter {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java JsonToSQLiteImporter <jsonFile> <sqliteDB>");
            return;
        }

        String jsonFile = "database/" + args[0]; // data.json in database folder
        String sqliteDB = "database/" + args[1]; // garbage_reporting.db in database folder

        try {
            importJsonToSQLite(jsonFile, sqliteDB);
            System.out.println("✅ JSON data imported into SQLite successfully.");
        } catch (Exception e) {
            System.err.println("❌ Failed to import JSON into SQLite: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void importJsonToSQLite(String jsonFile, String sqliteDB) throws Exception {
        String jsonContent = Files.readString(Paths.get(jsonFile));
        JSONObject data = new JSONObject(jsonContent);
        
        Connection conn = DriverManager.getConnection("jdbc:sqlite:" + sqliteDB);

        // Import users
        if (data.has("users")) {
            JSONArray users = data.getJSONArray("users");
            String userSQL = "INSERT OR REPLACE INTO users (user_id, name, email, password, address) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement userPs = conn.prepareStatement(userSQL);
            
            for (int i = 0; i < users.length(); i++) {
                JSONObject user = users.getJSONObject(i);
                userPs.setString(1, user.getString("user_id"));
                userPs.setString(2, user.getString("name"));
                userPs.setString(3, user.getString("email"));
                userPs.setString(4, user.getString("password"));
                userPs.setString(5, user.getString("address"));
                userPs.executeUpdate();
            }
            userPs.close();
        }

        // Import admin
        if (data.has("admin")) {
            JSONArray admin = data.getJSONArray("admin");
            String adminSQL = "INSERT OR REPLACE INTO admin (admin_id, username, password) VALUES (?, ?, ?)";
            PreparedStatement adminPs = conn.prepareStatement(adminSQL);
            
            for (int i = 0; i < admin.length(); i++) {
                JSONObject adminObj = admin.getJSONObject(i);
                adminPs.setString(1, adminObj.getString("admin_id"));
                adminPs.setString(2, adminObj.getString("username"));
                adminPs.setString(3, adminObj.getString("password"));
                adminPs.executeUpdate();
            }
            adminPs.close();
        }

        // Import garbage_types
        if (data.has("garbage_types")) {
            JSONArray types = data.getJSONArray("garbage_types");
            String typeSQL = "INSERT OR REPLACE INTO garbage_types (type_id, type_name) VALUES (?, ?)";
            PreparedStatement typePs = conn.prepareStatement(typeSQL);
            
            for (int i = 0; i < types.length(); i++) {
                JSONObject type = types.getJSONObject(i);
                typePs.setInt(1, type.getInt("type_id"));
                typePs.setString(2, type.getString("type_name"));
                typePs.executeUpdate();
            }
            typePs.close();
        }

        // Import reports
        if (data.has("reports")) {
            JSONArray reports = data.getJSONArray("reports");
            String reportSQL = "INSERT OR REPLACE INTO reports (report_id, user_id, waste_address, type_id, description, status, report_date) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement reportPs = conn.prepareStatement(reportSQL);
            
            for (int i = 0; i < reports.length(); i++) {
                JSONObject report = reports.getJSONObject(i);
                reportPs.setInt(1, report.getInt("report_id"));
                reportPs.setString(2, report.getString("user_id"));
                reportPs.setString(3, report.getString("waste_address"));
                reportPs.setInt(4, report.getInt("type_id"));
                reportPs.setString(5, report.optString("description", ""));
                reportPs.setString(6, report.optString("status", "pending"));
                reportPs.setString(7, report.optString("report_date", ""));
                reportPs.executeUpdate();
            }
            reportPs.close();
        }

        conn.close();
    }
}