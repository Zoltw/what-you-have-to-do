package efs.task.todoapp.repository;

import com.google.gson.Gson;
import efs.task.todoapp.AnotherUserException;
import efs.task.todoapp.service.handlers.task.FrontTask;

import java.util.UUID;

public class TaskEntity {
    String username;
    String password;
    String description;
    String due;

    UUID id;
    public TaskEntity() {}
    public TaskEntity(FrontTask frontTask, UserEntity userEntity) {
        username = userEntity.getUsername();
        password = userEntity.getPassword();
        description = frontTask.getDescription();
        due = frontTask.getDate();
        id = getUuidNew();
    }
    public String getUsername() {
        return username;
    }
    public String getDescription() {
        return description;
    }
    public String getPassword() {
        return password;
    }
    public String getDate() {
        return due;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUuidNew() {
        return UUID.randomUUID();
    }

    public UUID getUuid() {
        return id;
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setDate(String date) {
        this.due = date;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public void validateUser(String username) throws AnotherUserException {
        if (!username.equals(this.username)) {
            throw new AnotherUserException();
        }
    }
}
