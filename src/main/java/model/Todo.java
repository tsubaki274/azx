package model;

import java.sql.Date;

/**
 * Represents a single TODO entry.
 */
public class Todo {
    private int id;
    private String userId;
    private String todoForm;
    private boolean done;
    private Date limitDay;
    private String priority;

    public Todo() {
    }

    public Todo(int id, String userId, String todoForm, boolean done, Date limitDay, String priority) {
        this.id = id;
        this.userId = userId;
        this.todoForm = todoForm;
        this.done = done;
        this.limitDay = limitDay;
        this.priority = priority;
    }

    public Todo(String userId, String todoForm, Date limitDay, String priority) {
        this(0, userId, todoForm, false, limitDay, priority);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTodoForm() {
        return todoForm;
    }

    public void setTodoForm(String todoForm) {
        this.todoForm = todoForm;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public Date getLimitDay() {
        return limitDay;
    }

    public void setLimitDay(Date limitDay) {
        this.limitDay = limitDay;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}
