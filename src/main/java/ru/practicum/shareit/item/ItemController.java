package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public Item creatItem(@RequestHeader("X-Sharer-User-Id") int id, @RequestBody Item item) {
        return itemService.creatItem(id, item);
    }

    @PatchMapping(value = "/{itemId}")
    public Item updateItem(@RequestHeader("X-Sharer-User-Id") int idUser, @RequestBody Item item, @PathVariable("itemId") int id) {
        return itemService.updateItem(idUser, item, id);
    }

    @GetMapping(value = "/{itemId}")
    public ItemDto getItemById(@PathVariable("itemId") int id, @RequestHeader("X-Sharer-User-Id") int idUser) {
        return itemService.getItemById(id, idUser);
    }

    @GetMapping
    public List<ItemDto> getItemAll(@RequestHeader("X-Sharer-User-Id") int idUser) {
        return itemService.getItemAll(idUser);
    }

    @GetMapping(value = "/search")
    public List<ItemDto> getSearch(@RequestParam String text, @RequestHeader("X-Sharer-User-Id") int idUser) {
        return itemService.getSearch(text, idUser);
    }
}
