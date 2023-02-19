package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ItemDtoInletOutlet {
    private String name;
    private String description;
    private Boolean available;
    private Integer requestId;
}
