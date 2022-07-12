package efs.task.todoapp.repository;

import efs.task.todoapp.service.UnathorizedAccessException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class UserRepository implements Repository<String, UserEntity> {

    Map<String, UserEntity> users = new HashMap<>();
    @Override
    public String save(UserEntity userEntity) {
        if (userEntity == null) {
            return null;
        }
        String auth = userEntity.getUsername();
        if (users.containsKey(auth)) {
            return null;
        }
        users.put(auth, userEntity);
        return auth;
    }

    @Override
    public UserEntity query(String username) {
        return users.get(username);
    }

    @Override
    public List<UserEntity> query(Predicate<UserEntity> condition) {
        return new ArrayList<>(users.values());
    }

    @Override
    public UserEntity update(String s, UserEntity userEntity) {
        UserEntity userUpdate = users.get(s);
        String username = userEntity.getUsername();
        String password = userEntity.getPassword();

        if (username != null && !username.isBlank()) {
            userUpdate.setUsername(username);
        }

        if (password != null && !password.isBlank()) {
            userUpdate.setPassword(password);
        }
        return userUpdate;
    }

    @Override
    public boolean delete(String s) {
        return (users.remove(s) != null);
    }

    public UserEntity addUser(Map<String, String> previousUser) throws UnathorizedAccessException {
        UserEntity user = new UserEntity();
        String username = previousUser.get("username");
        String password = previousUser.get("password");

        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            throw new UnathorizedAccessException();
        }
        user.setUsername(username);
        user.setPassword(password);

        return user;
    }


}
