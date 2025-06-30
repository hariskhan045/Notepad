package note__pad_final;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseManager {

    // --- IMPORTANT: CONFIGURE YOUR DATABASE CONNECTION DETAILS HERE ---
    private static final String URL = "jdbc:mysql://localhost:3306/notepad_db";
    private static final String USER = "root"; // <-- Change this
    private static final String PASSWORD = "Fatmagul10@"; // <-- Change this

    /**
     * Establishes a connection to the database.
     * @return A Connection object.
     * @throws SQLException if a database access error occurs.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * Saves a note to the database.
     * @param title The title of the note.
     * @param content The content of the note.
     * @return true if the note was saved successfully, false otherwise.
     */
    public static boolean saveNote(String title, String content) {
        // SQL query to insert a new note into the 'notes' table
        String sql = "INSERT INTO notes (title, content) VALUES (?, ?)";

        // Try-with-resources to ensure the connection and statement are closed automatically
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Set the parameters for the query
            pstmt.setString(1, title);
            pstmt.setString(2, content);

            // Execute the update and get the number of rows affected
            int affectedRows = pstmt.executeUpdate();

            // Return true if at least one row was inserted
            return affectedRows > 0;

        } catch (SQLException e) {
            // Print the stack trace for debugging purposes
            e.printStackTrace();
            return false;
        }
    }
}