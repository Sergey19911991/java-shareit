package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.exeption.RequestException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.MappingItem;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
public class ItemRepository {
    private static final Map<Integer, Item> items = new HashMap<>();
    private final UserRepository userRepository;

    private final MappingItem mappingItem;


    private int number = 1;

    @Autowired
    public ItemRepository(UserRepository userRepository, MappingItem mappingItem) {
        this.userRepository = userRepository;
        this.mappingItem = mappingItem;
    }


    public Item creatItem(int id, Item item) {
        validationItem(item);
        userRepository.notFoundUser(userRepository.getUser(id));
        item.setId(number);
        item.setOwner(id);
        items.put(number, item);
        log.info("Создана вещь с id = {}", number);
        number = number + 1;
        return item;
    }

    public Item updateItem(int idUser, Item item, int id) {
        userRepository.notFoundUser(userRepository.getUser(idUser));
        if (idUser == items.get(id).getOwner()) {
            if (!(item.getName() == null)) {
                items.get(id).setName(item.getName());
            }
            if (!(item.getDescription() == null)) {
                items.get(id).setDescription(item.getDescription());
            }
            if (!(item.getAvailable() == null)) {
                items.get(id).setAvailable(item.getAvailable());
            }
            log.info("Перезаписана вещь с id = {}", number);
            return items.get(id);
        } else {
            log.error("Данная вещь не принадлежит пользователю");
            throw new NotFoundException("Данная вещь не принадлежит пользователю");
        }
    }

    public ItemDto getItemById(int id, int idUser) {
        log.error("Выведена информация о вещи с id = {}", id);
        return mappingItem.mapItemDto(items.get(id));
    }

    public List<ItemDto> getItemAll(int idUser) {
        log.error("Выведена информация о вещи пользователя с id = {}", idUser);
        List<ItemDto> itemsList = new ArrayList<>();
        for (Item itemId : items.values()) {
            if (itemId.getOwner() == idUser) {
                itemsList.add(mappingItem.mapItemDto(itemId));
            }
        }
        return itemsList;
    }

    public List<ItemDto> getSearch(String text, int idUser) {
        log.error("Выведена информация о вещях, содержащи в названии и/или в пописании текст {}", text);
        List<ItemDto> itemsList = new ArrayList<>();
        if (!text.isEmpty()) {
            for (Item itemId : items.values()) {
                if ((itemId.getName().toLowerCase().contains(text.toLowerCase()) || itemId.getDescription().toLowerCase().contains(text.toLowerCase())) && itemId.getAvailable() == true) {
                    itemsList.add(mappingItem.mapItemDto(itemId));
                }
            }
        }
        return itemsList;
    }


    private void validationItem(Item item) {
        if (item.getAvailable() == null) {
            log.error("Статус не может отсутствовать");
            throw new RequestException("Статус не может отсутствовать");
        }
        if (item.getName().isEmpty()) {
            log.error("Название вещи не может быть пустым");
            throw new RequestException("Название вещи не может быть пустым");
        }
        if (item.getDescription() == null) {
            log.error("Описание вещи не может быть пустым");
            throw new RequestException("Описание вещи не может быть пустым");
        }
    }

}
