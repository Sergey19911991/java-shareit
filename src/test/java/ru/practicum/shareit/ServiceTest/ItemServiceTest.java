package ru.practicum.shareit.ServiceTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.BookingsRepository;
import ru.practicum.shareit.item.CommenRepository;
import ru.practicum.shareit.item.ItemRepositoryJpa;
import ru.practicum.shareit.item.ItemServiceImpl;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.RequestRepository;
import ru.practicum.shareit.user.UserRepositoryJpa;

import java.util.*;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {

    private ItemRepositoryJpa itemRepositoryJpa;

    private UserRepositoryJpa userRepositoryJpa;
    private ItemServiceImpl itemServiceImpl;

    private MappingComment mappingComment;

    private BookingsRepository bookingsRepository;
    private CommenRepository commenRepository;
    private MappingItem mappingItem;

    private MappingNextBooking mappingNextBooking;

    private MappingLastBooking mappingLastBooking;
    private MappingItemDtoOutletInlet mappingItemDtoOutletInlet;

    private RequestRepository requestRepository;

    @BeforeEach
    void setUp() {
        itemRepositoryJpa = Mockito.mock(ItemRepositoryJpa.class);
        userRepositoryJpa = Mockito.mock(UserRepositoryJpa.class);
        mappingItemDtoOutletInlet = Mockito.mock(MappingItemDtoOutletInlet.class);
        mappingItem = Mockito.mock(MappingItem.class);
        itemServiceImpl = new ItemServiceImpl(itemRepositoryJpa, userRepositoryJpa, mappingComment,
                bookingsRepository, commenRepository, mappingItem, mappingNextBooking, mappingLastBooking, mappingItemDtoOutletInlet, requestRepository);
    }


    @Test
    public void createItem() {
        ItemDtoInletOutlet itemDtoInletOutlet = new ItemDtoInletOutlet();
        itemDtoInletOutlet.setName("Item");
        itemDtoInletOutlet.setDescription("Item");
        itemDtoInletOutlet.setAvailable(true);

        Item item = new Item();
        item.setId(1);
        item.setName("Item");
        item.setAvailable(true);
        item.setDescription("Item");

        when(mappingItemDtoOutletInlet.mapping(itemDtoInletOutlet)).thenReturn(item);
        when(userRepositoryJpa.existsById(1)).thenReturn(true);
        when(itemRepositoryJpa.save(item)).thenReturn(item);

        ItemDtoInletOutlet itemDtoInletOutlet1 = itemServiceImpl.creatItem(1, itemDtoInletOutlet);
        itemDtoInletOutlet.setId(1);

        Assertions.assertEquals(itemDtoInletOutlet, itemDtoInletOutlet1);

    }

    @Test
    public void getItem() {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(1);
        itemDto.setName("Item");
        itemDto.setDescription("Item");
        itemDto.setAvailable(true);

        Item item = new Item();
        item.setOwner(2);

        when(itemRepositoryJpa.findById(1)).thenReturn(Optional.of(item));
        when(itemRepositoryJpa.existsById(1)).thenReturn(true);
        when(mappingItem.mapItemDto(item)).thenReturn(itemDto);

        Assertions.assertEquals(itemDto, itemServiceImpl.getItemById(1, 1));
    }

    @Test
    public void getItemAll() {
        Set<ItemDto> list = new TreeSet<>(Comparator.comparingInt(ItemDto::getId));
        Item item = new Item();
        item.setOwner(2);
        List<Item> listItem = new ArrayList<>();
        listItem.add(item);

        when(itemRepositoryJpa.findAll()).thenReturn(listItem);

        Assertions.assertEquals(list, itemServiceImpl.getItemAll(1));
    }

    @Test
    public void getSearchItem() {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(1);
        itemDto.setName("Item");
        itemDto.setDescription("Item");
        itemDto.setAvailable(true);

        List<ItemDto> list = new ArrayList<>();
        list.add(itemDto);

        List<Item> listItem = new ArrayList<>();
        Item item = new Item();
        item.setId(1);
        item.setName("Item");
        item.setAvailable(true);
        item.setName("Item");
        listItem.add(item);

        when(itemRepositoryJpa.findAll()).thenReturn(listItem);
        when(mappingItem.mapItemDto(item)).thenReturn(itemDto);

        Assertions.assertEquals(list, itemServiceImpl.getSearch("tem", 1));
    }

    @Test
    public void createComment() {

    }

}
