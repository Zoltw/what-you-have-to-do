package efs.task.todoapp.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import efs.task.todoapp.AnotherUserException;
import efs.task.todoapp.IncorrectData;
import efs.task.todoapp.NotFoundException;
import efs.task.todoapp.repository.TaskEntity;
import efs.task.todoapp.repository.TaskRepository;
import efs.task.todoapp.repository.UserEntity;
import efs.task.todoapp.repository.UserRepository;
import efs.task.todoapp.service.handlers.task.FrontTask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static efs.task.todoapp.service.handlers.user.UserEntityConverter.checkAuth;
import static efs.task.todoapp.service.handlers.user.UserEntityConverter.convertToEntity;

public class ToDoService {
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    private final static Gson gson = new Gson();

    public ToDoService(UserRepository userRepository, TaskRepository taskRepository) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }

    public String saveUser(String userJson) throws UnathorizedAccessException, IncorrectData {
        Map<String, String> previousUser = convertToEntity(userJson);
        String savedUser = userRepository.save(userRepository.addUser(previousUser));

        if (savedUser == null) {
            throw new IncorrectData();
        }
        return savedUser;
    }

    public UserEntity getUser(Headers requestHeaders) throws UnathorizedAccessException, NotFoundException, IncorrectData {
        String[] userData = checkAuth(requestHeaders);
        UserEntity userEntity = getUserData(userData);
        UserEntity userFromRepository = userRepository.query(userEntity.getUsername());

        if(userFromRepository == null) {
            throw new NotFoundException();
        }
        if(!userFromRepository.getPassword().equals(userEntity.getPassword())) {
            throw new NotFoundException();
        }
        if(!userFromRepository.getUsername().equals(userEntity.getUsername())) {
            throw new NotFoundException();
        }
        return userFromRepository;
    }

    public String createTask(UserEntity userEntity, String taskString) throws IncorrectData, UnathorizedAccessException {

        if(taskString.isBlank()) {
            throw new IncorrectData();
        }

        FrontTask frontTask;
        try {
            frontTask = gson.fromJson(taskString, FrontTask.class);
        } catch(JsonSyntaxException e) {
            throw new IncorrectData();
        }

        if (frontTask == null || frontTask.getDescription() == null) {
            throw new IncorrectData();
        }

        if (frontTask.getDate() != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat.setLenient(false);
            try {
                dateFormat.parse(frontTask.getDate().trim());
            } catch (ParseException incorrect) {
                throw new IncorrectData();
            }
        }

        TaskEntity taskEntity = new TaskEntity(frontTask, userEntity);
        UUID uuid = taskRepository.save(taskEntity);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", uuid.toString());
        return gson.toJson(jsonObject);
    }


    public String getTask(UserEntity userEntity) throws UnathorizedAccessException, IncorrectData {
        List<TaskEntity> taskEntities = taskRepository.query(taskEntity -> taskEntity.getUsername().equals(userEntity.getUsername()));

        if (taskEntities == null) {
            throw new UnathorizedAccessException();
        }

        JsonArray jsonArray = new JsonArray();
        for(TaskEntity taskEntity: taskEntities) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("id", taskEntity.getUuid().toString());
            jsonObject.addProperty("description", taskEntity.getDescription());
            if(taskEntity.getDate() != null) {
                jsonObject.addProperty("due", taskEntity.getDate());
            }
            jsonArray.add(jsonObject);
        }
        return gson.toJson(jsonArray);
    }

    public String getChosenTask(String exchange, UserEntity userEntity) throws UnathorizedAccessException, AnotherUserException, NotFoundException, IncorrectData {
        final UUID taskId = getTaskId(exchange, false);
        TaskEntity taskEntity;
        try {
            taskEntity = taskRepository.query(UUID.fromString(String.valueOf(taskId)));
        } catch (IllegalArgumentException e) {
            throw new NotFoundException();
        }

        if(taskEntity == null) {
            throw new NotFoundException();
        }

        if(!taskEntity.getUsername().equals(userEntity.getUsername())) {
            throw new AnotherUserException();
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", taskEntity.getUuid().toString());
        jsonObject.addProperty("description", taskEntity.getDescription());
        if (taskEntity.getDate() != null) {
            jsonObject.addProperty("due", taskEntity.getDate());
        }
        return gson.toJson(jsonObject);

    }
    public String putTask(String exchange, String body, HttpExchange httpExchange) throws UnathorizedAccessException, IncorrectData, NotFoundException, AnotherUserException {
        final UUID taskId = getTaskId(exchange, false);
        final Map<String, String> taskData = convertToEntity(body);

        TaskEntity task = taskRepository.query(taskId);

        if(task == null) {
            throw new UnathorizedAccessException();
        }

        String[] userData = checkAuth(httpExchange.getRequestHeaders());
        UserEntity userEntity = getUserData(userData);
        task.validateUser(userEntity.getUsername());

        task.setDescription(taskData.get("description"));
        task.setDate(taskData.get("due"));
        task = taskRepository.update(taskId, task);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", task.getUuid().toString());
        jsonObject.addProperty("description", task.getDescription());
        if (task.getDate() != null) {
            jsonObject.addProperty("due", task.getDate());
        }
        return gson.toJson(jsonObject);
    }

    private UserEntity getUserData(String[] userData) throws UnathorizedAccessException {
        String username = userData[0];
        String password = userData[1];
        UserEntity user = userRepository.query(username);
        if (user == null){
            throw new UnathorizedAccessException();
        }

        if (!user.getPassword().equals(password)){
            throw new UnathorizedAccessException();
        }
        return user;
    }


    private UUID getTaskId(String address, boolean canBeEmpty) throws IncorrectData {
        try {
            final String[] splitAddress = address.split("/");
            if (splitAddress.length < 4) {
                if (canBeEmpty) {
                    return null;
                }
                throw new Exception();
            }
            final String taskId = splitAddress[splitAddress.length - 1];
            if (taskId == null || taskId.isBlank()) {
                throw new Exception();
            }
            return UUID.fromString(taskId);

        } catch (Exception e) {
            throw new IncorrectData();
        }
    }

    public void deleteTask(String path, UserEntity userEntity) throws UnathorizedAccessException, AnotherUserException, NotFoundException, IncorrectData {
        final UUID taskId = getTaskId(path, false);
        TaskEntity task = taskRepository.query(taskId);

        if(task == null) {
            throw new NotFoundException();
        }

        task.validateUser(userEntity.getUsername());

        taskRepository.delete(taskId);
    }
}
