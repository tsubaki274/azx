package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    private static final String DEFAULT_URL = "jdbc:h2:tcp://localhost/~/todoapp";
    private static final String DEFAULT_USER = "sa";
    private static final String DEFAULT_PASS = "";

    private static final String JDBC_URL = System.getenv().getOrDefault("TODO_JDBC_URL", DEFAULT_URL);
    private static final String DB_USER = System.getenv().getOrDefault("TODO_DB_USER", DEFAULT_USER);
    private static final String DB_PASS = System.getenv().getOrDefault("TODO_DB_PASSWORD", DEFAULT_PASS);
    private static final String JDBC_DRIVER = System.getenv().getOrDefault("TODO_JDBC_DRIVER",
            "org.h2.Driver");

    static {
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("JDBC driver not found", e);
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS);
    }

    public boolean authenticate(String userId, String password) {
        String sql = "SELECT 1 FROM USERS WHERE USER_ID = ? AND PASS = ?";
        try (Connection con = getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, userId);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to authenticate user", e);
        }
    }

    public boolean exists(String userId) {
        String sql = "SELECT 1 FROM USERS WHERE USER_ID = ?";
        try (Connection con = getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to check user existence", e);
        }
    }

    public boolean register(String userId, String password) {
        String sql = "INSERT INTO USERS (USER_ID, PASS) VALUES (?, ?)";
        try (Connection con = getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, userId);
            ps.setString(2, password);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to register user", e);
        }
    }
}
