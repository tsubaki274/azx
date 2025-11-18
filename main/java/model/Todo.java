package model;

import java.sql.Date;

public class Todo {
    private final int id;
    private final String todoForm;
    private final boolean done;
    private final Date limitDay;
    private final int priority;

    public Todo(int id, String todoForm, boolean done, Date limitDay, int priority) {
        this.id = id;
        this.todoForm = todoForm;
        this.done = done;
        this.limitDay = limitDay;
        this.priority = priority;
    }

    public int getId() {
        return id;
    }

    public String getTodoForm() {
        return todoForm;
    }

    public boolean isDone() {
        return done;
    }

    public Date getLimitDay() {
        return limitDay;
    }

    public int getPriority() {
        return priority;
    }
}
