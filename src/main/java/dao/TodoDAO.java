package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Todo;

public class TodoDAO {
    private final String JDBC_URL = "jdbc:h2:tcp://localhost/~/todoapp";
    private static final String DEFAULT_USER = "sa";
    private static final String DEFAULT_PASS = "";

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

    public boolean insert(Todo todo) {
        String sql = "INSERT INTO TODO (USER_ID, TODO_FORM, DONE, LIMIT_DAY, PRIORITY) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, todo.getUserId());
            ps.setString(2, todo.getTodoForm());
            ps.setBoolean(3, todo.isDone());
            ps.setDate(4, todo.getLimitDay());
            ps.setString(5, normalizePriorityForStorage(todo.getPriority()));
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert TODO", e);
        }
    }

    public List<Todo> findByUser(String userId, String filter) {
        StringBuilder sql = new StringBuilder(
                "SELECT TODO_ID, USER_ID, TODO_FORM, DONE, LIMIT_DAY, PRIORITY FROM TODO WHERE USER_ID = ?");
        if ("active".equals(filter)) {
            sql.append(" AND DONE = false");
        } else if ("done".equals(filter)) {
            sql.append(" AND DONE = true");
        }
        sql.append(" ORDER BY LIMIT_DAY ASC, ");
        sql.append("CASE PRIORITY ");
        sql.append(" WHEN '強' THEN 3");
        sql.append(" WHEN '中' THEN 2");
        sql.append(" WHEN '弱' THEN 1");
        sql.append(" WHEN '5' THEN 3");
        sql.append(" WHEN '4' THEN 3");
        sql.append(" WHEN '3' THEN 2");
        sql.append(" WHEN '2' THEN 1");
        sql.append(" WHEN '1' THEN 1");
        sql.append(" ELSE 0 END DESC, TODO_ID DESC");

        List<Todo> todos = new ArrayList<>();
        try (Connection con = getConnection();
                PreparedStatement ps = con.prepareStatement(sql.toString())) {
            ps.setString(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Todo todo = new Todo(
                            rs.getInt("TODO_ID"),
                            rs.getString("USER_ID"),
                            rs.getString("TODO_FORM"),
                            rs.getBoolean("DONE"),
                            rs.getDate("LIMIT_DAY"),
                            normalizePriorityForDisplay(rs.getString("PRIORITY")));
                    todos.add(todo);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load TODO list", e);
        }
        return todos;
    }

    public void updateDone(int todoId, String userId, boolean done) {
        String sql = "UPDATE TODO SET DONE = ? WHERE TODO_ID = ? AND USER_ID = ?";
        try (Connection con = getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setBoolean(1, done);
            ps.setInt(2, todoId);
            ps.setString(3, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update TODO status", e);
        }
    }

    public void delete(int todoId, String userId) {
        String sql = "DELETE FROM TODO WHERE TODO_ID = ? AND USER_ID = ?";
        try (Connection con = getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, todoId);
            ps.setString(2, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete TODO", e);
        }
    }

    public void clearDone(String userId) {
        String sql = "DELETE FROM TODO WHERE USER_ID = ? AND DONE = true";
        try (Connection con = getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to clear completed TODO", e);
        }
    }

    private String normalizePriorityForStorage(String rawPriority) {
        String normalized = normalizePriorityForDisplay(rawPriority);
        return normalized == null ? "中" : normalized;
    }

    private String normalizePriorityForDisplay(String rawPriority) {
        if (rawPriority == null || rawPriority.isBlank()) {
            return "中";
        }
        String trimmed = rawPriority.trim();
        switch (trimmed) {
            case "強":
            case "5":
            case "4":
                return "強";
            case "中":
            case "3":
                return "中";
            case "弱":
            case "2":
            case "1":
                return "弱";
            default:
                return trimmed;
        }
    }

}
