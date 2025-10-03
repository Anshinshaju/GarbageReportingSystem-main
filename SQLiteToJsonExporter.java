import org.json.JSONArray;
import org.json.JSONObject;
import java.io.FileWriter;
import java.sql.*;

public class SQLiteToJsonExporter {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java SQLiteToJsonExporter <sqliteDB> <jsonFile>");
            return;
        }

        String sqliteDB = "database/" + args[0]; // garbage_reporting.db in database folder
        String jsonFile = "database/" + args[1]; // exported_data.json in database folder

        try {
            exportSQLiteToJson(sqliteDB, jsonFile);
            System.out.println("✅ SQLite data exported to JSON successfully.");
        } catch (Exception e) {
            System.err.println("❌ Failed to export SQLite to JSON: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void exportSQLiteToJson(String sqliteDB, String jsonFile) throws Exception {
        Connection conn = DriverManager.getConnection("jdbc:sqlite:" + sqliteDB);
        JSONObject exportData = new JSONObject();

        // Export users
        Statement userStmt = conn.createStatement();
        ResultSet userRs = userStmt.executeQuery("SELECT user_id, name, email, password, address FROM users");
        JSONArray usersArray = new JSONArray();
        while (userRs.next()) {
            JSONObject user = new JSONObject();
            user.put("user_id", userRs.getString("user_id"));
            user.put("name", userRs.getString("name"));
            user.put("email", userRs.getString("email"));
            user.put("password", userRs.getString("password"));
            user.put("address", userRs.getString("address"));
            usersArray.put(user);
        }
        exportData.put("users", usersArray);
        userRs.close();
        userStmt.close();

        // Export admin
        Statement adminStmt = conn.createStatement();
        ResultSet adminRs = adminStmt.executeQuery("SELECT admin_id, username, password FROM admin");
        JSONArray adminArray = new JSONArray();
        while (adminRs.next()) {
            JSONObject admin = new JSONObject();
            admin.put("admin_id", adminRs.getString("admin_id"));
            admin.put("username", adminRs.getString("username"));
            admin.put("password", adminRs.getString("password"));
            adminArray.put(admin);
        }
        exportData.put("admin", adminArray);
        adminRs.close();
        adminStmt.close();

        // Export garbage_types
        Statement typeStmt = conn.createStatement();
        ResultSet typeRs = typeStmt.executeQuery("SELECT type_id, type_name FROM garbage_types");
        JSONArray typesArray = new JSONArray();
        while (typeRs.next()) {
            JSONObject type = new JSONObject();
            type.put("type_id", typeRs.getInt("type_id"));
            type.put("type_name", typeRs.getString("type_name"));
            typesArray.put(type);
        }
        exportData.put("garbage_types", typesArray);
        typeRs.close();
        typeStmt.close();

        // Export reports
        Statement reportStmt = conn.createStatement();
        ResultSet reportRs = reportStmt.executeQuery("SELECT report_id, user_id, waste_address, type_id, description, status, report_date FROM reports");
        JSONArray reportsArray = new JSONArray();
        while (reportRs.next()) {
            JSONObject report = new JSONObject();
            report.put("report_id", reportRs.getInt("report_id"));
            report.put("user_id", reportRs.getString("user_id"));
            report.put("waste_address", reportRs.getString("waste_address"));
            report.put("type_id", reportRs.getInt("type_id"));
            report.put("description", reportRs.getString("description"));
            report.put("status", reportRs.getString("status"));
            report.put("report_date", reportRs.getString("report_date"));
            reportsArray.put(report);
        }
        exportData.put("reports", reportsArray);
        reportRs.close();
        reportStmt.close();

        conn.close();

        // Save JSON file to database folder
        try (FileWriter file = new FileWriter(jsonFile)) {
            file.write(exportData.toString(4));
        }
    }
}