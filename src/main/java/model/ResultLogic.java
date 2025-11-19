package model;

import java.util.List;

import dao.TodoDAO;

public class ResultLogic {
    private final TodoDAO todoDAO = new TodoDAO();

    public List<Todo> fetchTodos(String userId, String filter) {
        return todoDAO.findByUser(userId, filter);
    }

    public void updateDone(String userId, int todoId, boolean done) {
        todoDAO.updateDone(todoId, userId, done);
    }

    public void delete(String userId, int todoId) {
        todoDAO.delete(todoId, userId);
    }

    public void clearDone(String userId) {
        todoDAO.clearDone(userId);
    }
}
