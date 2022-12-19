package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;

import java.util.ArrayList;
import java.util.List;


public interface ItemService {

    Item creatItem(int id,Item item);

    Item updateItem(int idUser,Item item,int id);

    Item getItemById(int id, int idUser);

    List<Item> getItemAll(int idUser);

    List<Item> getSearch (String text, int idUser);
}
