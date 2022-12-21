package ru.practicum.shareit.item.model;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Data
public class Item {
     private int id;
     private String name;
     private String description;
     private Boolean available;
     private int owner;
}
