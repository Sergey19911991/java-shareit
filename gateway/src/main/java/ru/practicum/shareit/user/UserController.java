package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.RequestException;


import javax.validation.Valid;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {

    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> creatUser(@RequestBody @Valid User user) {
        if (user.getEmail() == null) {
            log.error("Пользователь не создан. У пользователя нет email.");
            throw new RequestException("У пользователя должен быть email!");
        }
        if (!user.getEmail().contains("@")) {
            log.error("Пользователь не создан. Неправильный email.");
            throw new RequestException("Неправильный email!");
        }
        log.info("Creating user {}", user);
        return userClient.creatUser(user);
    }

    @PatchMapping(value = "/{userId}")
    public ResponseEntity<Object> patchUser(@RequestBody @Valid User user, @PathVariable("userId") int id) {
        log.info("Patching userId{}, user {}", id, user);
        return userClient.patchUser(user, id);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUser(@PathVariable("userId") int userId) {
        log.info("Get userId={}", userId);
        return userClient.getUser(userId);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> delUser(@PathVariable("userId") int userId) {
        log.info("Del userId={}", userId);
        return userClient.delUser(userId);
    }

    @GetMapping("")
    public ResponseEntity<Object> getUsers() {
        log.info("Get all users");
        return userClient.getUsers();
    }
}
