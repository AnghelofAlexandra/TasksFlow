package ro.a.tasksflow;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable
{
    private final int id;
    private String username;
    private String mail;
    private ArrayList<Task> tasks = new ArrayList<>();

    public User(int id, String username, String mail, ArrayList<Task> tasks) {
        this.id = id;
        this.username = username;
        this.mail = mail;
        this.tasks = tasks;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public void addTask(Task t)
    {
        this.tasks.add(t);
    }
}
