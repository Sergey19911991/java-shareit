package ru.practicum.shareit.request.dto;

import lombok.Data;
import ru.practicum.shareit.item.dto.ItemDtoInletOutlet;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ItemRequestDtoOutlet {
    private int id;
    private LocalDateTime created;
    private String description;
    private List<ItemDtoInletOutlet> items;
}
