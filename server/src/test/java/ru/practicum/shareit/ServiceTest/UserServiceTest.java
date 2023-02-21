package ru.practicum.shareit.ServiceTest;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.RequestException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepositoryJpa;
import ru.practicum.shareit.user.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    private UserRepositoryJpa userRepositoryJpa;

    private UserService userService;

    private User user;

    private List<User> userList;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;


    @BeforeEach
    void setUp() {
        userRepositoryJpa = Mockito.mock(UserRepositoryJpa.class);
        userService = new UserService(userRepositoryJpa);
        user = new User();
        user.setId(1);
        user.setName("User");
        user.setEmail("email@email.ru");
        userList = new ArrayList<>();
        userList.add(user);
    }

    @Test
    public void createUser() {
        when(userRepositoryJpa.save(any())).thenReturn(user);

        Assertions.assertEquals(user, userService.creatUser(user));
    }


    @Test
    public void getUser() {
        when(userRepositoryJpa.findById(1)).thenReturn(Optional.of(user));

        Assertions.assertEquals(user, userService.getUser(1));
    }


    @Test
    public void getUserNull() {
        User user1 = new User();
        user1 = null;

        when(userRepositoryJpa.findById(1)).thenReturn(Optional.ofNullable(user1));

        Assertions.assertThrows(NotFoundException.class, () -> {
            userService.getUser(1);
        });
    }

    @Test
    public void getAll() {
        when(userRepositoryJpa.findAll()).thenReturn(userList);

        Assertions.assertEquals(userList, userService.getAllUser());
    }

    @Test
    public void updateUser() {
        User user1 = new User();
        user1.setEmail("emailUser@email.ru");

        when(userRepositoryJpa.findById(anyInt())).thenReturn(Optional.of(user));
        when(userRepositoryJpa.existsById(anyInt())).thenReturn(true);
        User user2 = userService.updateUser(user1, 1);
        verify(userRepositoryJpa).save(userArgumentCaptor.capture());
        User saved = userArgumentCaptor.getValue();

        assertEquals("User", saved.getName());
        assertEquals("emailUser@email.ru", saved.getEmail());

    }

    @Test
    public void deleteUser() {
        when(userRepositoryJpa.existsById(anyInt())).thenReturn(true);
        userService.deleteUser(anyInt());

        verify(userRepositoryJpa).deleteById(anyInt());
    }

    @Test
    public void deleteUserNull() {
        when(userRepositoryJpa.existsById(anyInt())).thenReturn(false);

        Assertions.assertThrows(NotFoundException.class, () -> {
            userService.deleteUser(anyInt());
            ;
        });
    }

    @Test
    public void updateError() {
        when(userRepositoryJpa.existsById(anyInt())).thenReturn(false);

        Assertions.assertThrows(NotFoundException.class, () -> {
            userService.updateUser(user, 1);
        });
    }

    @Test
    public void updateEmailNull() {
        User user1 = new User();
        user1.setName("NameName");

        when(userRepositoryJpa.findById(anyInt())).thenReturn(Optional.of(user));
        when(userRepositoryJpa.existsById(anyInt())).thenReturn(true);
        User user2 = userService.updateUser(user1, 1);
        verify(userRepositoryJpa).save(userArgumentCaptor.capture());
        User saved = userArgumentCaptor.getValue();

        assertEquals("NameName", saved.getName());
        assertEquals("email@email.ru", saved.getEmail());

    }

    @Test
    public void validationUser() {
        user.setEmail(null);

        Assertions.assertThrows(RequestException.class, () -> {
            userService.validationUserEmail(user);
        });
    }

    @Test
    public void validationEmail() {
        user.setEmail("email");

        Assertions.assertThrows(RequestException.class, () -> {
            userService.validationUserEmail(user);
        });
    }

}
