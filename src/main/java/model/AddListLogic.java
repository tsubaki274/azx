package model;

import java.sql.Date;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import dao.TodoDAO;

public class AddListLogic {
    private final TodoDAO todoDAO = new TodoDAO();

    public Todo validateAndBuild(String userId,
            String todoForm,
            String yearParam,
            String monthParam,
            String dayParam,
            String priorityParam,
            List<String> errors) {
        if (todoForm == null || todoForm.isBlank()) {
            errors.add("タスク名を入力してください。");
        }

        String priority = parsePriority(priorityParam, errors);
        LocalDate targetDate = parseDate(yearParam, monthParam, dayParam, errors);

        if (!errors.isEmpty()) {
            return null;
        }

        Todo todo = new Todo();
        todo.setUserId(userId);
        todo.setTodoForm(todoForm.trim());
        todo.setDone(false);
        todo.setLimitDay(Date.valueOf(targetDate));
        todo.setPriority(priority);
        return todo;
    }

    public boolean register(Todo todo) {
        return todoDAO.insert(todo);
    }

    private String parsePriority(String priorityParam, List<String> errors) {
        final String defaultPriority = "中";
        if (priorityParam == null || priorityParam.isBlank()) {
            errors.add("優先度を選択してください。");
            return defaultPriority;
        }

        String normalized = normalizePriority(priorityParam.trim());
        if (normalized == null) {
            errors.add("優先度は「弱」「中」「強」から選択してください。");
            return defaultPriority;
        }
        return normalized;
    }

    private String normalizePriority(String value) {
        return switch (value) {
            case "弱" -> "弱";
            case "中" -> "中";
            case "強" -> "強";
            default -> null;
        };
    }

    private LocalDate parseDate(String yearParam, String monthParam, String dayParam, List<String> errors) {
        try {
            int year = Integer.parseInt(yearParam);
            int month = Integer.parseInt(monthParam);
            int day = Integer.parseInt(dayParam);
            return LocalDate.of(year, month, day);
        } catch (DateTimeException | NumberFormatException e) {
            errors.add("期限の日付が不正です。");
            return LocalDate.now();
        }
    }

    public List<String> createErrorList() {
        return new ArrayList<>();
    }
}
