package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.RequestException;

import javax.validation.Valid;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> createItem(@RequestHeader("X-Sharer-User-Id") int userId,
                                             @RequestBody @Valid ItemDtoInletOutlet itemDtoInletOutlet) {
        if (itemDtoInletOutlet.getAvailable() == null) {
            log.error("Статус не может отсутствовать");
            throw new RequestException("Статус не может отсутствовать");
        }
        if (itemDtoInletOutlet.getName().isEmpty()) {
            log.error("Название вещи не может быть пустым");
            throw new RequestException("Название вещи не может быть пустым");
        }
        if (itemDtoInletOutlet.getDescription() == null) {
            log.error("Описание вещи не может быть пустым");
            throw new RequestException("Описание вещи не может быть пустым");
        }
        log.info("Creating item {}, userId={}", itemDtoInletOutlet, userId);
        return itemClient.createItem(userId, itemDtoInletOutlet);
    }

    @PatchMapping(value = "/{itemId}")
    public ResponseEntity<Object> patchItem(@RequestHeader("X-Sharer-User-Id") int userId,
                                            @RequestBody @Valid ItemDtoInletOutlet itemDtoInletOutlet, @PathVariable("itemId") int id) {
        log.info("Patching itemId{}, item {}", id, itemDtoInletOutlet);
        return itemClient.patchItem(itemDtoInletOutlet, id, userId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(@PathVariable("itemId") int id, @RequestHeader("X-Sharer-User-Id") int userId) {
        log.info("Get itemId {}, userId {}", id, userId);
        return itemClient.getItem(id, userId);
    }

    @GetMapping("")
    public ResponseEntity<Object> getItems(@RequestHeader("X-Sharer-User-Id") int userId) {
        log.info("Get all users userId {}", userId);
        return itemClient.getItems(userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> getItemsSearch(@RequestHeader("X-Sharer-User-Id") int userId,
                                                 @RequestParam String text) {
        return itemClient.getItemsSearch(userId, text);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@RequestHeader("X-Sharer-User-Id") int userId,
                                                @RequestBody @Valid Comment comment,
                                                @PathVariable("itemId") int itemId) {
        return itemClient.createComment(userId, comment, itemId);
    }

}
