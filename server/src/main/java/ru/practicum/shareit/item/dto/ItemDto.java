package ru.practicum.shareit.item.dto;

import lombok.Data;

import java.util.List;

@Data
public class ItemDto {
    private int id;
    private String name;
    private String description;
    private Boolean available;
    private List<CommentDto> comments;
    private LastBooking lastBooking;
    private NextBooking nextBooking;
}
