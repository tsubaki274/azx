package model;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dao.TodoDao;

public class TodoService {

    private final TodoDao todoDao = new TodoDao();

    public TodoPage loadTodos(String userId, String filterParam) {
        String normalizedFilter = normalizeFilter(filterParam);
        Boolean doneFilter = toBooleanFilter(normalizedFilter);
        List<Todo> todos = todoDao.findByUser(userId, doneFilter);
        return new TodoPage(normalizedFilter, todos);
    }

    public AddTodoResult addTodo(String userId, AddTodoCommand command) {
        List<String> errors = new ArrayList<>();

        String name = command.todoForm();
        if (name == null || name.isBlank()) {
            errors.add("タスク名を入力してください");
        }

        LocalDate limitDate = null;
        try {
            limitDate = LocalDate.of(
                    Integer.parseInt(command.year()),
                    Integer.parseInt(command.month()),
                    Integer.parseInt(command.day()));
        } catch (Exception e) {
            errors.add("正しい期限を選択してください");
        }

        int priority = 0;
        try {
            priority = Integer.parseInt(command.priority());
            if (priority < 1 || priority > 5) {
                errors.add("重要度は1〜5で入力してください");
            }
        } catch (NumberFormatException e) {
            errors.add("重要度は数値で入力してください");
        }

        if (!errors.isEmpty()) {
            return AddTodoResult.failure(errors, limitDate);
        }

        todoDao.insert(userId, name.trim(), Date.valueOf(limitDate), priority);
        return AddTodoResult.success(limitDate);
    }

    public void toggleTodo(String userId, int id, boolean done) {
        todoDao.updateDone(id, done, userId);
    }

    public void deleteTodo(String userId, int id) {
        todoDao.delete(id, userId);
    }

    public void clearCompleted(String userId) {
        todoDao.deleteCompleted(userId);
    }

    public String buildFilterQuery(String filterParam) {
        if (filterParam == null || filterParam.isBlank()) {
            return "";
        }
        return "?filter=" + normalizeFilter(filterParam);
    }

    private String normalizeFilter(String filter) {
        if (filter == null) {
            return "all";
        }
        return switch (filter) {
            case "active", "done" -> filter;
            default -> "all";
        };
    }

    private Boolean toBooleanFilter(String filter) {
        return switch (filter) {
            case "active" -> Boolean.FALSE;
            case "done" -> Boolean.TRUE;
            default -> null;
        };
    }

    public record TodoPage(String filter, List<Todo> todos) {
        public TodoPage {
            todos = Collections.unmodifiableList(new ArrayList<>(todos));
        }
    }

    public record AddTodoCommand(String todoForm, String year, String month, String day, String priority) {
    }

    public static final class AddTodoResult {
        private final boolean success;
        private final List<String> errors;
        private final LocalDate limitDate;

        private AddTodoResult(boolean success, List<String> errors, LocalDate limitDate) {
            this.success = success;
            this.errors = Collections.unmodifiableList(new ArrayList<>(errors));
            this.limitDate = limitDate;
        }

        public static AddTodoResult success(LocalDate limitDate) {
            return new AddTodoResult(true, List.of(), limitDate);
        }

        public static AddTodoResult failure(List<String> errors, LocalDate limitDate) {
            return new AddTodoResult(false, errors, limitDate);
        }

        public boolean isSuccess() {
            return success;
        }

        public List<String> getErrors() {
            return errors;
        }

        public LocalDate getLimitDate() {
            return limitDate;
        }
    }
}
