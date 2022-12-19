package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.exeption.RequestException;
import ru.practicum.shareit.exeption.ValidationExeption;

import java.util.*;

@Slf4j
@Repository
public class UserRepository {
    private static final Map<Integer, User> users = new HashMap<>();
    private int number = 1;


    public User creatUser(User user) {
        validationUserEmail(user);
        user.setId(number);
        users.put(number, user);
        log.info("Создан пользователь с id = {}", number);
        number = number + 1;
        return user;
    }

    public User updateUser(User user, int id) {
        if (!(user.getEmail() == null)) {
            validationUserEmail(user);
            users.get(id).setEmail(user.getEmail());
        }
        if (!(user.getName() == null)) {
            users.get(id).setName(user.getName());
        }
        log.info("Изменены данные пользователя с id = {}", id);
        return users.get(id);
    }

    public User getUser(int id) {
        log.info("Выведены данные пользователя с id = {}", id);
        return users.get(id);
    }

    public void deleteUser(int id) {
        log.info("Удален пользователь с id = {}", id);
        users.remove(id);
    }

    public List<User> getAllUser() {
        log.info("Выведены данные о всех пользователях");
        List<User> usersList = new ArrayList<>();
        for (User userId : users.values()) {
            usersList.add(userId);
        }
        return usersList;
    }


    private void validationUserEmail(User user) {
        if (user.getEmail() == null) {
            log.error("Пользователь не создан. У пользователя нет email.");
            throw new RequestException("У пользователя должен быть email!");
        }
        if (!user.getEmail().contains("@")) {
            log.error("Пользователь не создан. Неправильный email.");
            throw new RequestException("Неправильный email!");
        }
        for (User userId : users.values()) {
            if (userId.getEmail().equals(user.getEmail())) {
                log.error("Пользователь не создан. У двух пользователей не можем быть одинаковый email.");
                throw new ValidationExeption("У двух пользователей не можем быть одинаковый email!");
            }
        }

    }

    public void notFoundUser(User user) {
        if (!users.containsValue(user)) {
            log.error("Такой пользователь не найден");
            throw new NotFoundException("Такой пользователь не найден!");
        }
    }

}
