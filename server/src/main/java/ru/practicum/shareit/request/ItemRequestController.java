package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoOutlet;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final RequestServiceImpl requestService;

    @PostMapping
    public ItemRequest createRequest(@RequestBody ItemRequestDto itemRequestDto, @RequestHeader("X-Sharer-User-Id") int userId) {
        return requestService.createRequest(itemRequestDto, userId);
    }

    @GetMapping
    public List<ItemRequestDtoOutlet> getRequests(@RequestHeader("X-Sharer-User-Id") int userId) {
        return requestService.getRequests(userId);
    }

    @GetMapping(value = "/all")
    public List<ItemRequestDtoOutlet> getRequestsAll(@RequestHeader("X-Sharer-User-Id") int userId,
                                                     @RequestParam(value = "from", required = false) Integer from,
                                                     @RequestParam(value = "size", required = false) Integer size) {
        return requestService.getRequestsAll(userId, from, size);
    }

    @GetMapping(value = "/{requestId}")
    public ItemRequestDtoOutlet getRequestById(@RequestHeader("X-Sharer-User-Id") int idUser, @PathVariable("requestId") int idRequest) {
        return requestService.getRequestById(idUser, idRequest);
    }
}
