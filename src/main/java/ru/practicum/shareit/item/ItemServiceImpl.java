package ru.practicum.shareit.item;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;

    @Override
    public Item creatItem(int id, Item item) {
        return itemRepository.creatItem(id, item);
    }

    @Override
    public Item updateItem(int idUser, Item item, int id) {
        return itemRepository.updateItem(idUser, item, id);
    }

    @Override
    public Item getItemById(int id, int idUser) {
        return itemRepository.getItemById(id, idUser);
    }

    @Override
    public List<Item> getItemAll(int idUser) {
        return itemRepository.getItemAll(idUser);
    }

    @Override
    public List<Item> getSearch(String text, int idUser) {
        return itemRepository.getSearch(text, idUser);
    }
}




