package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoInletOutlet;
import ru.practicum.shareit.item.model.Comments;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemServiceImpl itemServiceImpl;

    @PostMapping
    public ItemDtoInletOutlet creatItem(@RequestHeader("X-Sharer-User-Id") int id, @RequestBody ItemDtoInletOutlet item) {
        return itemServiceImpl.creatItem(id, item);
    }

    @PatchMapping(value = "/{itemId}")
    public Item updateItem(@RequestHeader("X-Sharer-User-Id") int idUser, @RequestBody Item item, @PathVariable("itemId") int id) {
        return itemServiceImpl.updateItem(idUser, item, id);
    }

    @GetMapping(value = "/{itemId}")
    public ItemDto getItemById(@PathVariable("itemId") int id, @RequestHeader("X-Sharer-User-Id") int idUser) {
        return itemServiceImpl.getItemById(id, idUser);
    }

    @GetMapping
    public Set<ItemDto> getItemAll(@RequestHeader("X-Sharer-User-Id") int idUser) {
        return itemServiceImpl.getItemAll(idUser);
    }

    @GetMapping(value = "/search")
    public List<ItemDto> getSearch(@RequestParam String text, @RequestHeader("X-Sharer-User-Id") int idUser) {
        return itemServiceImpl.getSearch(text, idUser);
    }

    @PostMapping(value = "{itemId}/comment")
    public CommentDto createComment(@RequestBody Comments comment, @RequestHeader("X-Sharer-User-Id") int booker, @PathVariable("itemId") int itemId) {
        return itemServiceImpl.createComment(comment, booker, itemId);
    }
}
