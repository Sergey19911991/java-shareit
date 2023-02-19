package ru.practicum.shareit.item.dto;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;

@Service
public class MappingItem {

    //из item в dto
    public ItemDto mapItemDto(Item item) {
        ItemDto dto = new ItemDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setAvailable(item.getAvailable());
        return dto;
    }

}
