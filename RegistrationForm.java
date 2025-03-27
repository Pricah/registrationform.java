 import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class RegistrationForm {
    private JFrame frame;
    private JTextField nameField, emailField, phoneField;
    private JButton submitButton, viewButton;
    private JTable table;
    private DefaultTableModel tableModel;
    
    // Database Connection Details
    private static final String URL = "jdbc:mysql://localhost:3306/registration_db";
    private static final String USER = "root";  // Change this if your MySQL username is different
    private static final String PASSWORD = "";  // Change this if you have a MySQL password

    public RegistrationForm() {
        frame = new JFrame("Registration Form");
        frame.setSize(500, 400);
        frame.setLayout(new GridLayout(5, 2));

        // Labels and Input Fields
        frame.add(new JLabel("Name:"));
        nameField = new JTextField();
        frame.add(nameField);

        frame.add(new JLabel("Email:"));
        emailField = new JTextField();
        frame.add(emailField);

        frame.add(new JLabel("Phone:"));
        phoneField = new JTextField();
        frame.add(phoneField);

        // Buttons
        submitButton = new JButton("Register");
        viewButton = new JButton("View Records");

        frame.add(submitButton);
        frame.add(viewButton);

        // Table for viewing records
        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);
        tableModel.addColumn("ID");
        tableModel.addColumn("Name");
        tableModel.addColumn("Email");
        tableModel.addColumn("Phone");

        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane);

        // Button Actions
        submitButton.addActionListener(e -> registerUser());
        viewButton.addActionListener(e -> loadRecords());

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private void registerUser() {
        String name = nameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "All fields must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO users (name, email, phone) VALUES (?, ?, ?)")) {

            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, phone);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(frame, "User Registered Successfully!");
            nameField.setText("");
            emailField.setText("");
            phoneField.setText("");

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Database Error!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadRecords() {
        tableModel.setRowCount(0);

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM users")) {

            while (rs.next()) {
                tableModel.addRow(new Object[]{rs.getInt("id"), rs.getString("name"), rs.getString("email"), rs.getString("phone")});
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Database Error!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    public static void main(String[] args) {
        try {
            // Load MySQL Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            new RegistrationForm();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "MySQL Driver not found!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
