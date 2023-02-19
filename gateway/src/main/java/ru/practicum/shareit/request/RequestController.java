package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.RequestException;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class RequestController {

    private final RequestClient requestClient;

    @PostMapping("")
    public ResponseEntity<Object> createRequest(@RequestHeader("X-Sharer-User-Id") int userId,
                                                @RequestBody ItemRequestDto itemRequestDto) {
        return requestClient.createRequest(userId, itemRequestDto);
    }

    @GetMapping("")
    public ResponseEntity<Object> getRequests(@RequestHeader("X-Sharer-User-Id") int userId) {
        return requestClient.getRequests(userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequest(@RequestHeader("X-Sharer-User-Id") int userId,
                                             @PathVariable("requestId") int id) {
        return requestClient.getRequest(userId, id);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getRequestsAll(@RequestHeader("X-Sharer-User-Id") long userId,
                                                 @RequestParam(value = "from", defaultValue = "0") Integer from,
                                                 @RequestParam(value = "size", defaultValue = "10") Integer size) {
        if (from < 0 || size <= 0) {
            throw new RequestException("Длина списка не может быть меньше нуля или равна нулю");
        }
        return requestClient.getRequestAll(userId, from, size);
    }
}
