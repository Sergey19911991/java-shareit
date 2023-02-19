package ru.practicum.shareit.item.dto;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;

@Slf4j
@Service
public class MappingItemDtoOutletInlet {
    //из dto в item

    public Item mapping(ItemDtoInletOutlet itemDtoInletOutlet) {
        Item item = new Item();
        item.setDescription(itemDtoInletOutlet.getDescription());
        item.setName(itemDtoInletOutlet.getName());
        item.setAvailable(itemDtoInletOutlet.getAvailable());
        return item;
    }

    //из item в dto
    public ItemDtoInletOutlet mappingItem(Item item) {
        ItemDtoInletOutlet itemDtoInletOutlet = new ItemDtoInletOutlet();
        itemDtoInletOutlet.setId(item.getId());
        itemDtoInletOutlet.setName(item.getName());
        itemDtoInletOutlet.setDescription(item.getDescription());
        itemDtoInletOutlet.setAvailable(item.getAvailable());
        itemDtoInletOutlet.setRequestId(item.getRequest().getId());
        return itemDtoInletOutlet;
    }
}
