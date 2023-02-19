package ru.practicum.shareit.ServiceTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.exeption.RequestException;
import ru.practicum.shareit.item.ItemRepositoryJpa;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.RequestRepository;
import ru.practicum.shareit.request.RequestServiceImpl;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoOutlet;
import ru.practicum.shareit.request.dto.MappingRequest;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepositoryJpa;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RequestServiceImplTest {

    private MappingRequest mappingRequest;
    private UserRepositoryJpa userRepositoryJpa;

    private RequestRepository requestRepository;

    private ItemRepositoryJpa itemRepositoryJpa;

    private MappingItemDtoOutletInlet mappingItemDtoOutletInlet;

    private RequestServiceImpl requestService;

    @BeforeEach
    void setUp() {
        itemRepositoryJpa = Mockito.mock(ItemRepositoryJpa.class);
        userRepositoryJpa = Mockito.mock(UserRepositoryJpa.class);
        mappingItemDtoOutletInlet = Mockito.mock(MappingItemDtoOutletInlet.class);
        requestRepository = Mockito.mock(RequestRepository.class);
        mappingRequest = Mockito.mock(MappingRequest.class);
        requestService = new RequestServiceImpl(mappingRequest, userRepositoryJpa, requestRepository, itemRepositoryJpa,
                mappingItemDtoOutletInlet);
    }

    @Test
    public void createError() {
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        User user = new User();
        user = null;

        when(userRepositoryJpa.findById(1)).thenReturn(Optional.ofNullable(user));

        Assertions.assertThrows(NotFoundException.class, () -> {
            requestService.createRequest(itemRequestDto, 1);
        });
    }

    @Test
    public void create() {
        User user = new User();
        user.setId(1);

        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("Text");

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription("Text");
        itemRequest.setRequestor(user);
        itemRequest.setCreated(LocalDateTime.now());


        when(mappingRequest.mapItemDto(itemRequestDto)).thenReturn(itemRequest);
        when(userRepositoryJpa.findById(1)).thenReturn(Optional.of(user));
        when(requestRepository.save(itemRequest)).thenReturn(itemRequest);

        Assertions.assertEquals(itemRequest, requestService.createRequest(itemRequestDto, 1));
    }

    @Test
    public void getRequestsTest() {
        when(userRepositoryJpa.existsById(1)).thenReturn(false);

        Assertions.assertThrows(NotFoundException.class, () -> {
            requestService.getRequests(1);
        });
    }

    @Test
    public void getRequestsTest1() {
        List<ItemRequestDtoOutlet> itemRequestList = new ArrayList<>();

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1);
        List<ItemRequest> itemRequests = new ArrayList<>();
        itemRequests.add(itemRequest);

        Item item = new Item();
        item.setId(1);
        List<Item> itemList = new ArrayList<>();
        itemList.add(item);

        ItemDtoInletOutlet itemDtoInletOutlet = new ItemDtoInletOutlet();
        itemDtoInletOutlet.setId(1);
        itemDtoInletOutlet.setName("Name");
        itemDtoInletOutlet.setDescription("Text");
        itemDtoInletOutlet.setAvailable(true);
        itemDtoInletOutlet.setRequestId(1);
        List<ItemDtoInletOutlet> itemDtoInletOutlets = new ArrayList<>();
        itemDtoInletOutlets.add(itemDtoInletOutlet);

        ItemRequestDtoOutlet itemRequestDtoOutlet = new ItemRequestDtoOutlet();
        itemRequestDtoOutlet.setItems(itemDtoInletOutlets);
        itemRequestList.add(itemRequestDtoOutlet);


        when(requestRepository.getRequestRequestor(1)).thenReturn(itemRequests);
        when(userRepositoryJpa.existsById(1)).thenReturn(true);
        when(itemRepositoryJpa.getRequest(1)).thenReturn(itemList);
        when(mappingItemDtoOutletInlet.mappingItem(item)).thenReturn(itemDtoInletOutlet);
        when(mappingRequest.mapItemDtoOutlet(itemRequest)).thenReturn(itemRequestDtoOutlet);

        Assertions.assertEquals(itemRequestList, requestService.getRequests(1));
    }

    @Test
    public void getRequestByIdTest() {
        when(userRepositoryJpa.existsById(1)).thenReturn(false);

        Assertions.assertThrows(NotFoundException.class, () -> {
            requestService.getRequestById(1, 1);
        });
    }

    @Test
    public void getRequestByIdTest1() {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest = null;

        when(userRepositoryJpa.existsById(1)).thenReturn(true);
        when(requestRepository.findById(1)).thenReturn(Optional.ofNullable(itemRequest));

        Assertions.assertThrows(NotFoundException.class, () -> {
            requestService.getRequestById(1, 1);
        });
    }

    @Test
    public void getRequestByIdTest2() {
        Item item = new Item();
        item.setId(1);
        List<Item> itemList = new ArrayList<>();
        itemList.add(item);

        ItemDtoInletOutlet itemDtoInletOutlet = new ItemDtoInletOutlet();
        itemDtoInletOutlet.setId(1);
        itemDtoInletOutlet.setName("Name");
        itemDtoInletOutlet.setDescription("Text");
        itemDtoInletOutlet.setAvailable(true);
        itemDtoInletOutlet.setRequestId(1);

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1);


        ItemRequestDtoOutlet itemRequestDtoOutlet = new ItemRequestDtoOutlet();
        itemRequestDtoOutlet.setId(1);
        itemRequestDtoOutlet.setDescription("Text");
        itemRequestDtoOutlet.setCreated(LocalDateTime.now());

        List<ItemDtoInletOutlet> itemDtoInletOutlets = new ArrayList<>();
        itemDtoInletOutlets.add(itemDtoInletOutlet);

        itemRequestDtoOutlet.setItems(itemDtoInletOutlets);


        when(userRepositoryJpa.existsById(1)).thenReturn(true);
        when(requestRepository.findById(1)).thenReturn(Optional.of(itemRequest));
        when(mappingRequest.mapItemDtoOutlet(itemRequest)).thenReturn(itemRequestDtoOutlet);
        when(itemRepositoryJpa.getRequest(1)).thenReturn(itemList);
        when(mappingItemDtoOutletInlet.mappingItem(item)).thenReturn(itemDtoInletOutlet);

        Assertions.assertEquals(itemRequestDtoOutlet, requestService.getRequestById(1, 1));
    }

    @Test
    public void getRequestAllTest() {
        Assertions.assertThrows(RequestException.class, () -> {
            requestService.getRequestsAll(1, -1, 1);
        });
    }

    @Test
    public void getRequestAllTest1() {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1);
        List<ItemRequest> itemRequestsList = new ArrayList<>();
        itemRequestsList.add(itemRequest);

        Item item = new Item();
        List<Item> itemList = new ArrayList<>();
        itemList.add(item);

        ItemDtoInletOutlet itemDtoInletOutlet = new ItemDtoInletOutlet();
        itemDtoInletOutlet.setId(1);
        itemDtoInletOutlet.setName("Name");
        itemDtoInletOutlet.setAvailable(true);
        itemDtoInletOutlet.setDescription("Text");
        itemDtoInletOutlet.setRequestId(1);

        List<ItemDtoInletOutlet> itemDtoInletOutlets = new ArrayList<>();
        itemDtoInletOutlets.add(itemDtoInletOutlet);

        ItemRequestDtoOutlet itemRequestDtoOutlet = new ItemRequestDtoOutlet();
        itemRequestDtoOutlet.setId(1);
        itemRequestDtoOutlet.setDescription("Text");
        itemRequestDtoOutlet.setCreated(LocalDateTime.now());
        itemRequestDtoOutlet.setItems(itemDtoInletOutlets);

        List<ItemRequestDtoOutlet> itemRequestList = new ArrayList<>();
        itemRequestList.add(itemRequestDtoOutlet);

        when(requestRepository.getRequestAll(1)).thenReturn(itemRequestsList);
        when(itemRepositoryJpa.getRequest(1)).thenReturn(itemList);
        when(mappingItemDtoOutletInlet.mappingItem(item)).thenReturn(itemDtoInletOutlet);
        when(mappingRequest.mapItemDtoOutlet(itemRequest)).thenReturn(itemRequestDtoOutlet);

        Assertions.assertEquals(itemRequestList, requestService.getRequestsAll(1, null, null));

    }

    @Test
    public void getRequestAllTest2() {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1);
        List<ItemRequest> itemRequestsList = new ArrayList<>();
        itemRequestsList.add(itemRequest);

        Item item = new Item();
        List<Item> itemList = new ArrayList<>();
        itemList.add(item);

        ItemDtoInletOutlet itemDtoInletOutlet = new ItemDtoInletOutlet();
        itemDtoInletOutlet.setId(1);
        itemDtoInletOutlet.setName("Name");
        itemDtoInletOutlet.setAvailable(true);
        itemDtoInletOutlet.setDescription("Text");
        itemDtoInletOutlet.setRequestId(1);

        List<ItemDtoInletOutlet> itemDtoInletOutlets = new ArrayList<>();
        itemDtoInletOutlets.add(itemDtoInletOutlet);

        ItemRequestDtoOutlet itemRequestDtoOutlet = new ItemRequestDtoOutlet();
        itemRequestDtoOutlet.setId(1);
        itemRequestDtoOutlet.setDescription("Text");
        itemRequestDtoOutlet.setCreated(LocalDateTime.now());
        itemRequestDtoOutlet.setItems(itemDtoInletOutlets);

        List<ItemRequestDtoOutlet> itemRequestList = new ArrayList<>();
        itemRequestList.add(itemRequestDtoOutlet);

        when(requestRepository.getRequestAllFromSize(1, 0, 1)).thenReturn(itemRequestsList);
        when(itemRepositoryJpa.getRequest(1)).thenReturn(itemList);
        when(mappingItemDtoOutletInlet.mappingItem(item)).thenReturn(itemDtoInletOutlet);
        when(mappingRequest.mapItemDtoOutlet(itemRequest)).thenReturn(itemRequestDtoOutlet);

        Assertions.assertEquals(itemRequestList, requestService.getRequestsAll(1, 0, 1));

    }

}