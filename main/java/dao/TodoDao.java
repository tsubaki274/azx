package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Todo;

public class TodoDao {
    private static final String URL = "jdbc:h2:~/todoapp";
    private static final String USER = "sa";
    private static final String PASS = "";
    private static final String TABLE = "TODO";

    static {
        ensureTable();
    }

    public List<Todo> findByUser(String userId) {
        return findByUser(userId, null);
    }

    public List<Todo> findByUser(String userId, Boolean doneFilter) {
        List<Todo> list = new ArrayList<>();

        if (userId == null || userId.isBlank()) {
            return list;
        }

        StringBuilder sql = new StringBuilder("SELECT * FROM " + TABLE + " WHERE user_id=?");
        if (doneFilter != null) {
            sql.append(" AND done=?");
        }
        sql.append(" ORDER BY id DESC");

        try (Connection con = DriverManager.getConnection(URL, USER, PASS);
                PreparedStatement ps = con.prepareStatement(sql.toString())) {

            ps.setString(1, userId);
            if (doneFilter != null) {
                ps.setBoolean(2, doneFilter);
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Todo t = new Todo(
                        rs.getInt("id"),
                        rs.getString("todo_form"),
                        rs.getBoolean("done"),
                        rs.getDate("limit_day"),
                        rs.getInt("priority"));
                list.add(t);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void insert(String userId, String form, Date limit, int priority) {
        String sql = "INSERT INTO " + TABLE + "(user_id, todo_form, limit_day, priority) VALUES(?,?,?,?)";
        try (Connection con = DriverManager.getConnection(URL, USER, PASS);
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, userId);
            ps.setString(2, form);
            ps.setDate(3, limit);
            ps.setInt(4, priority);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id, String userId) {
        String sql = "DELETE FROM " + TABLE + " WHERE id=? AND user_id=?";
        try (Connection con = DriverManager.getConnection(URL, USER, PASS);
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setString(2, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateDone(int id, boolean done, String userId) {
        String sql = "UPDATE " + TABLE + " SET done=? WHERE id=? AND user_id=?";
        try (Connection con = DriverManager.getConnection(URL, USER, PASS);
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setBoolean(1, done);
            ps.setInt(2, id);
            ps.setString(3, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteCompleted(String userId) {
        String sql = "DELETE FROM " + TABLE + " WHERE user_id=? AND done=true";
        try (Connection con = DriverManager.getConnection(URL, USER, PASS);
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void ensureTable() {
        String ddl = "CREATE TABLE IF NOT EXISTS " + TABLE
                + " (id IDENTITY PRIMARY KEY, user_id VARCHAR(64) NOT NULL, todo_form VARCHAR(255) NOT NULL,"
                + " limit_day DATE, priority INT NOT NULL, done BOOLEAN DEFAULT FALSE)";

        try (Connection con = DriverManager.getConnection(URL, USER, PASS);
                PreparedStatement ps = con.prepareStatement(ddl)) {
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
