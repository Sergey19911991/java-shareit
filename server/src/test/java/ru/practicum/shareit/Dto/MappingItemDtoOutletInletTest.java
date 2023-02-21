package ru.practicum.shareit.Dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.ItemDtoInletOutlet;
import ru.practicum.shareit.item.dto.MappingItemDtoOutletInlet;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;

public class MappingItemDtoOutletInletTest {
    private MappingItemDtoOutletInlet mappingItemDtoOutletInlet;

    @BeforeEach
    void setUp() {
        mappingItemDtoOutletInlet = new MappingItemDtoOutletInlet();
    }

    @Test
    public void mappingItem() {
        ItemDtoInletOutlet itemDtoInletOutlet = new ItemDtoInletOutlet();
        itemDtoInletOutlet.setName("Item");
        itemDtoInletOutlet.setAvailable(true);
        itemDtoInletOutlet.setDescription("Item");

        Item item = new Item();
        item.setName("Item");
        item.setAvailable(true);
        item.setDescription("Item");

        Assertions.assertEquals(mappingItemDtoOutletInlet.mapping(itemDtoInletOutlet), item);
    }

    @Test
    public void mappingDto() {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1);

        Item item = new Item();
        item.setName("Item");
        item.setAvailable(true);
        item.setDescription("Item");
        item.setId(1);
        item.setRequest(itemRequest);

        ItemDtoInletOutlet itemDtoInletOutlet = new ItemDtoInletOutlet();
        itemDtoInletOutlet.setName("Item");
        itemDtoInletOutlet.setAvailable(true);
        itemDtoInletOutlet.setDescription("Item");
        itemDtoInletOutlet.setId(1);
        itemDtoInletOutlet.setRequestId(1);

        Assertions.assertEquals(mappingItemDtoOutletInlet.mappingItem(item), itemDtoInletOutlet);
    }
}
