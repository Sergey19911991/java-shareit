package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoOutlet;

import java.util.List;

public interface RequestService {
    ItemRequest createRequest(ItemRequestDto requestDto, int idUser);

    List<ItemRequestDtoOutlet> getRequests(int idUser);

    List<ItemRequestDtoOutlet> getRequestsAll(int idUser, Integer from, Integer size);

    ItemRequestDtoOutlet getRequestById(int userId, int requestId);
}
