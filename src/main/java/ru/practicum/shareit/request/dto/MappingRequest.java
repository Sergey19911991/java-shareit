package ru.practicum.shareit.request.dto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exeption.RequestException;
import ru.practicum.shareit.request.ItemRequest;

@Slf4j
@RequiredArgsConstructor
@Service
public class MappingRequest {

    //из dto
    public ItemRequest mapItemDto(ItemRequestDto itemRequestDto) {
        ItemRequest itemRequest = new ItemRequest();
        if (itemRequestDto.getDescription() != null) {
            itemRequest.setDescription(itemRequestDto.getDescription());
            return itemRequest;
        } else {
            log.error("Пустое описание запроса!");
            throw new RequestException("Пустое описание запроса!");
        }
    }


    //из item в dto_outlet
    public ItemRequestDtoOutlet mapItemDtoOutlet(ItemRequest itemRequest) {
        ItemRequestDtoOutlet itemRequestDtoOutlet = new ItemRequestDtoOutlet();
        itemRequestDtoOutlet.setId(itemRequest.getId());
        itemRequestDtoOutlet.setCreated(itemRequest.getCreated());
        itemRequestDtoOutlet.setDescription(itemRequest.getDescription());
        return itemRequestDtoOutlet;
    }

}
