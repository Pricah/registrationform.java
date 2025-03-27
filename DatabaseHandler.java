// DatabaseHandler.java
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class DatabaseHandler {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/registration_db";
    private static final String DB_USER = "root";  // Change if needed
    private static final String DB_PASSWORD = "";  // Change if needed

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    public static boolean insertUser(int id, String name, String gender, String address, String contact) {
        String query = "INSERT INTO users (id, name, gender, address, contact) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pst = conn.prepareStatement(query)) {
            
            pst.setInt(1, id);
            pst.setString(2, name);
            pst.setString(3, gender);
            pst.setString(4, address);
            pst.setString(5, contact);

            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database Error: " + e.getMessage());
            return false;
        }
    }

    public static void loadUsers(DefaultTableModel tableModel) {
        String query = "SELECT * FROM users";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            tableModel.setRowCount(0);  // Clear existing table data

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("gender"),
                    rs.getString("address"),
                    rs.getString("contact")
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error loading data: " + e.getMessage());
        }
    }
}
