package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;


public interface ItemService {

    Item creatItem(int id, Item item);

    Item updateItem(int idUser, Item item, int id);

    Item getItemById(int id, int idUser);

    List<Item> getItemAll(int idUser);

    List<Item> getSearch(String text, int idUser);
}
