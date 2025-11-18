package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {
    private static final String URL = "jdbc:h2:~/todoapp";
    private static final String USER = "sa";
    private static final String PASS = "";
    private static final String TABLE = "USERS";

    static {
        ensureTable();
    }

    public boolean login(String userId, String password) {
        String sql = "SELECT 1 FROM " + TABLE + " WHERE user_id=? AND password=?";
        try (Connection con = DriverManager.getConnection(URL, USER, PASS);
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, userId);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean exists(String userId) {
        String sql = "SELECT 1 FROM " + TABLE + " WHERE user_id=?";
        try (Connection con = DriverManager.getConnection(URL, USER, PASS);
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean register(String userId, String password) {
        String sql = "INSERT INTO " + TABLE + "(user_id, password) VALUES(?,?)";
        try (Connection con = DriverManager.getConnection(URL, USER, PASS);
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, userId);
            ps.setString(2, password);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    private static void ensureTable() {
        String ddl = "CREATE TABLE IF NOT EXISTS " + TABLE
                + " (user_id VARCHAR(64) PRIMARY KEY, password VARCHAR(255) NOT NULL)";
        try (Connection con = DriverManager.getConnection(URL, USER, PASS);
                PreparedStatement ps = con.prepareStatement(ddl)) {
            ps.execute();
        } catch (SQLException e) {
            // ignore so the app can still start; registration will fail if table is missing
        }
    }
}
