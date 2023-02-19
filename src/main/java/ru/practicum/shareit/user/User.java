package ru.practicum.shareit.user;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class User {
    private int id;
    private String name;
    private String email;
}
