package ro.a.tasksflow;

import java.io.Serializable;

public class Task implements Serializable
{
    private final int id;
    private String title;
    private String description;
    private String subject;
    private String deadline;
    private String priority;
    private boolean completed;

    static int idIndex = 1;

    public Task(String title, String description, String subject, String deadline, String priority, boolean completed) {
        this.id = idIndex++;
        this.title = title;
        this.description = description;
        this.subject = subject;
        this.deadline = deadline;
        this.priority = priority;
        this.completed = completed;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @Override
    public String toString() {
        return title + "    |   "
                + (completed ? "Done" : "Not yet");
    }
}
