package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoInletOutlet;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Set;

public interface ItemService {
    ItemDtoInletOutlet creatItem(int id, ItemDtoInletOutlet itemDtoInletOutlet);

    Item updateItem(int idUser, Item item, int id);

    ItemDto getItemById(int id, int idUser);

    Set<ItemDto> getItemAll(int idUser);

    List<ItemDto> getSearch(String text, int idUser);
}
