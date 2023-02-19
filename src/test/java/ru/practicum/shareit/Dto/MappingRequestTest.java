package ru.practicum.shareit.Dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.exeption.RequestException;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoOutlet;
import ru.practicum.shareit.request.dto.MappingRequest;

import java.time.LocalDateTime;

public class MappingRequestTest {

    private MappingRequest mappingRequest;

    @BeforeEach
    void setUp() {
        mappingRequest = new MappingRequest();
    }

    @Test
    public void mapItemDtoTest() {
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("Description");

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription("Description");

        Assertions.assertEquals(mappingRequest.mapItemDto(itemRequestDto), itemRequest);
    }

    @Test
    public void mapItemDtoTestError() {
        ItemRequestDto itemRequestDto = new ItemRequestDto();

        Assertions.assertThrows(RequestException.class, () -> {
            mappingRequest.mapItemDto(itemRequestDto);
        }, "Пустое описание запроса!");
    }

    @Test
    public void mapItemDtoOutletTest() {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription("Description");
        itemRequest.setId(1);
        itemRequest.setCreated(LocalDateTime.parse("1992-05-01T00:30:09"));

        ItemRequestDtoOutlet itemRequestDtoOutlet = new ItemRequestDtoOutlet();
        itemRequestDtoOutlet.setDescription("Description");
        itemRequestDtoOutlet.setId(1);
        itemRequestDtoOutlet.setCreated(LocalDateTime.parse("1992-05-01T00:30:09"));

        Assertions.assertEquals(mappingRequest.mapItemDtoOutlet(itemRequest), itemRequestDtoOutlet);
    }


}
