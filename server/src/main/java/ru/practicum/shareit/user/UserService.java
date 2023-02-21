package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.RequestException;
import ru.practicum.shareit.exception.ConflictException;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepositoryJpa userRepositoryJpa;


    public User creatUser(User user) {
        for (User user1 : userRepositoryJpa.findAll()) {
            if (user1.getName().equals(user.getName())) {
                User user2 = new User();
                user2.setName("1");
                user2.setEmail("@");
                userRepositoryJpa.save(user2);
                userRepositoryJpa.delete(user2);
                throw new ConflictException("Нельзя зарегистрировать два одинаковых пользователя!");
            }
        }
        log.info("Создан новый пользователь");
        return userRepositoryJpa.save(user);
    }

    public User updateUser(User user, int id) {
        if (userRepositoryJpa.existsById(id)) {
            if (user.getEmail() != null) {
                for (User user1 : userRepositoryJpa.findAll()) {
                    if (user1.getEmail().equals(user.getEmail())) {
                        throw new ConflictException("Нельзя зарегистрировать два одинаковых пользователя!");
                    }
                }
                validationUserEmail(user);
            } else {
                user.setEmail(userRepositoryJpa.findById(id).get().getEmail());
            }
            if (user.getName() == null) {
                user.setName(userRepositoryJpa.findById(id).get().getName());
            }
            user.setId(id);
            log.info("Перезаписан пользователь с id = {}", id);
            return userRepositoryJpa.save(user);
        } else {
            log.error("Такой пользователь не найден");
            throw new NotFoundException("Такой пользователь не найден!");
        }

    }

    public User getUser(int id) {
        User user = userRepositoryJpa.findById(id).orElse(null);
        if (user != null) {
            return userRepositoryJpa.findById(id).get();
        } else {
            log.error("Такой пользователь не найден");
            throw new NotFoundException("Такой пользователь не найден!");
        }

    }


    public void deleteUser(int id) {
        if (userRepositoryJpa.existsById(id)) {
            log.info("Удален пользователь с id = {}", id);
            userRepositoryJpa.deleteById(id);
        } else {
            log.error("Пользователь с таким id не найден");
            throw new NotFoundException("Пользователь с таким id не найден");
        }
    }

    public List<User> getAllUser() {
        return userRepositoryJpa.findAll();
    }

    public void validationUserEmail(User user) {
        if (user.getEmail() == null) {
            log.error("Пользователь не создан. У пользователя нет email.");
            throw new RequestException("У пользователя должен быть email!");
        }
        if (!user.getEmail().contains("@")) {
            log.error("Пользователь не создан. Неправильный email.");
            throw new RequestException("Неправильный email!");
        }
    }
}
