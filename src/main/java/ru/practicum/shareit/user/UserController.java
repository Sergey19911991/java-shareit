package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    public User creatUser(@RequestBody User user) {
        return userService.creatUser(user);
    }

    @PatchMapping(value = "/{userId}")
    public User updateUser(@RequestBody User user, @PathVariable("userId") int id) {
        return userService.updateUser(user, id);
    }

    @GetMapping(value = "/{userId}")
    public User getUser(@PathVariable("userId") int id) {
        return userService.getUser(id);
    }

    @DeleteMapping(value = "/{userId}")
    public void deleteUser(@PathVariable("userId") int id) {
        userService.deleteUser(id);
    }

    @GetMapping
    public List<User> getAllUser() {
        return userService.getAllUser();
    }
}
