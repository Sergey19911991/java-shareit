package ru.practicum.shareit.item.dto;

import lombok.Data;

@Data
public class ItemDtoInletOutlet {
    private int id;
    private String name;
    private String description;
    private Boolean available;
    private Integer requestId;
}
