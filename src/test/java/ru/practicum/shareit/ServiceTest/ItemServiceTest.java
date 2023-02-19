package ru.practicum.shareit.ServiceTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingsRepository;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.exeption.RequestException;
import ru.practicum.shareit.item.CommenRepository;
import ru.practicum.shareit.item.ItemRepositoryJpa;
import ru.practicum.shareit.item.ItemServiceImpl;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comments;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.RequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepositoryJpa;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {
    @Captor
    private ArgumentCaptor<Item> itemArgumentCaptor;
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
        bookingsRepository = Mockito.mock(BookingsRepository.class);
        requestRepository = Mockito.mock(RequestRepository.class);
        mappingNextBooking = Mockito.mock(MappingNextBooking.class);
        mappingLastBooking = Mockito.mock(MappingLastBooking.class);
        mappingComment = Mockito.mock(MappingComment.class);
        commenRepository = Mockito.mock(CommenRepository.class);
        itemServiceImpl = new ItemServiceImpl(itemRepositoryJpa, userRepositoryJpa, mappingComment,
                bookingsRepository, commenRepository, mappingItem, mappingNextBooking, mappingLastBooking, mappingItemDtoOutletInlet, requestRepository);
    }


    @Test
    public void createItem() {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1);

        ItemDtoInletOutlet itemDtoInletOutlet = new ItemDtoInletOutlet();
        itemDtoInletOutlet.setName("Item");
        itemDtoInletOutlet.setDescription("Item");
        itemDtoInletOutlet.setAvailable(true);
        itemDtoInletOutlet.setRequestId(1);

        Item item = new Item();
        item.setId(1);
        item.setName("Item");
        item.setAvailable(true);
        item.setDescription("Item");

        when(mappingItemDtoOutletInlet.mapping(itemDtoInletOutlet)).thenReturn(item);
        when(userRepositoryJpa.existsById(1)).thenReturn(true);
        when(itemRepositoryJpa.save(item)).thenReturn(item);
        when(requestRepository.findById(1)).thenReturn(Optional.of(itemRequest));

        ItemDtoInletOutlet itemDtoInletOutlet1 = itemServiceImpl.creatItem(1, itemDtoInletOutlet);
        itemDtoInletOutlet.setId(1);

        Assertions.assertEquals(itemDtoInletOutlet, itemDtoInletOutlet1);

    }

    @Test
    public void createItemNotFoundUser() {
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
        when(userRepositoryJpa.existsById(1)).thenReturn(false);

        Assertions.assertThrows(NotFoundException.class, () -> {
            itemServiceImpl.creatItem(1, itemDtoInletOutlet);
        });
    }


    @Test
    public void updateItemError() {
        Item item = new Item();
        item.setId(1);
        item.setOwner(1);

        Item item1 = new Item();

        when(itemRepositoryJpa.findById(1)).thenReturn(Optional.of(item));

        Assertions.assertThrows(NotFoundException.class, () -> {
            itemServiceImpl.updateItem(2, item1, 1);
        });
    }

    @Test
    public void updateItemTest() {
        Item item = new Item();
        item.setName(null);
        item.setAvailable(null);
        item.setDescription(null);

        Item item1 = new Item();
        item1.setId(1);
        item1.setName("Item");
        item1.setDescription("Text");
        item1.setAvailable(true);
        item1.setOwner(1);

        when(itemRepositoryJpa.findById(1)).thenReturn(Optional.of(item1));
        when(userRepositoryJpa.existsById(1)).thenReturn(true);

        Item item2 = itemServiceImpl.updateItem(1, item, 1);

        verify(itemRepositoryJpa).save(itemArgumentCaptor.capture());
        Item saved = itemArgumentCaptor.getValue();

        assertEquals("Item", saved.getName());
        assertEquals("Text", saved.getDescription());
        assertEquals(true, saved.getAvailable());

    }

    @Test
    public void getItem() {
        User user = new User();
        user.setId(2);

        ItemDto itemDto = new ItemDto();
        itemDto.setId(1);
        itemDto.setName("Item");
        itemDto.setDescription("Item");
        itemDto.setAvailable(true);

        Item item = new Item();
        item.setId(1);
        item.setOwner(1);

        Booking booking = new Booking();
        booking.setItem(item);
        booking.setStart(LocalDateTime.parse("1991-05-01T00:30:09"));
        booking.setEnd(LocalDateTime.parse("1992-05-01T00:30:09"));
        booking.setBooker(user);
        booking.setId(1);

        Booking booking1 = new Booking();
        booking1.setItem(item);
        booking1.setStart(LocalDateTime.parse("2024-05-01T00:30:09"));
        booking1.setEnd(LocalDateTime.parse("2025-05-01T00:30:09"));
        booking1.setBooker(user);
        booking1.setId(2);

        List<Booking> bookingList = new ArrayList<>();
        bookingList.add(booking1);
        bookingList.add(booking);

        NextBooking nextBooking = new NextBooking();
        nextBooking.setId(1);
        nextBooking.setBookerId(1);

        LastBooking lastBooking = new LastBooking();
        lastBooking.setId(2);
        lastBooking.setBookerId(1);

        itemDto.setLastBooking(lastBooking);
        itemDto.setNextBooking(nextBooking);

        when(itemRepositoryJpa.existsById(1)).thenReturn(true);
        when(itemRepositoryJpa.findById(1)).thenReturn(Optional.of(item));
        when(mappingItem.mapItemDto(item)).thenReturn(itemDto);
        when(bookingsRepository.getBookingItemOwner(1, 1)).thenReturn(bookingList);
        when(mappingLastBooking.lastBooking(booking)).thenReturn(lastBooking);
        when(mappingNextBooking.nextBooking(booking1)).thenReturn(nextBooking);


        Assertions.assertEquals(itemDto, itemServiceImpl.getItemById(1, 1));
    }

    @Test
    public void getItemError() {
        when(itemRepositoryJpa.existsById(1)).thenReturn(false);

        Assertions.assertThrows(NotFoundException.class, () -> {
            itemServiceImpl.getItemById(1, 1);
        });
    }

    @Test
    public void getItemAll() {
        User user = new User();
        user.setId(2);

        Set<ItemDto> list = new TreeSet<>(Comparator.comparingInt(ItemDto::getId));
        Item item = new Item();
        item.setOwner(1);
        item.setId(1);

        List<Item> listItem = new ArrayList<>();
        listItem.add(item);

        ItemDto itemDto = new ItemDto();
        itemDto.setId(1);
        itemDto.setDescription("Description");
        itemDto.setAvailable(true);
        itemDto.setName("Name");

        Booking booking = new Booking();
        booking.setItem(item);
        booking.setStart(LocalDateTime.parse("1991-05-01T00:30:09"));
        booking.setEnd(LocalDateTime.parse("1992-05-01T00:30:09"));
        booking.setBooker(user);
        booking.setId(1);

        Booking booking1 = new Booking();
        booking1.setItem(item);
        booking1.setStart(LocalDateTime.parse("2024-05-01T00:30:09"));
        booking1.setEnd(LocalDateTime.parse("2025-05-01T00:30:09"));
        booking1.setBooker(user);
        booking1.setId(2);

        List<Booking> bookingList = new ArrayList<>();
        bookingList.add(booking1);
        bookingList.add(booking);

        NextBooking nextBooking = new NextBooking();
        nextBooking.setId(1);
        nextBooking.setBookerId(1);

        LastBooking lastBooking = new LastBooking();
        lastBooking.setId(2);
        lastBooking.setBookerId(1);

        itemDto.setLastBooking(lastBooking);
        itemDto.setNextBooking(nextBooking);
        list.add(itemDto);

        when(itemRepositoryJpa.findAll()).thenReturn(listItem);
        when(mappingItem.mapItemDto(item)).thenReturn(itemDto);
        when(bookingsRepository.getBookingItemOwner(1, 1)).thenReturn(bookingList);
        when(mappingLastBooking.lastBooking(booking)).thenReturn(lastBooking);
        when(mappingNextBooking.nextBooking(booking1)).thenReturn(nextBooking);

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
    public void validationAvailableNull() {
        Item item = new Item();
        item.setId(1);
        item.setName("Item");
        item.setAvailable(null);
        item.setDescription("Item");

        Assertions.assertThrows(RequestException.class, () -> {
            itemServiceImpl.validationItem(item);
        });
    }

    @Test
    public void validationDescriptionNull() {
        Item item = new Item();
        item.setId(1);
        item.setName("Item");
        item.setAvailable(true);
        item.setDescription(null);

        Assertions.assertThrows(RequestException.class, () -> {
            itemServiceImpl.validationItem(item);
        });
    }

    @Test
    public void validationNameEmpty() {
        Item item = new Item();
        item.setId(1);
        item.setName("");
        item.setAvailable(true);
        item.setDescription("Text");

        Assertions.assertThrows(RequestException.class, () -> {
            itemServiceImpl.validationItem(item);
        });
    }

    @Test
    public void createCommentError() {
        Comments comment = new Comments();
        comment.setText("");

        Booking booking = new Booking();
        List<Booking> list = new ArrayList<>();
        list.add(booking);

        when(bookingsRepository.getBookingItemBooker(1, 1)).thenReturn(list);

        Assertions.assertThrows(RequestException.class, () -> {
            itemServiceImpl.createComment(comment, 1, 1);
        });

    }


    @Test
    public void createComment() {
        User user = new User();
        user.setId(1);
        user.setName("Name");

        CommentDto commentDto = new CommentDto();
        commentDto.setId(1);
        commentDto.setAuthorName("Name");
        commentDto.setText("Text");

        Item item = new Item();
        item.setId(1);

        Comments comment = new Comments();
        comment.setText("Text");

        Booking booking = new Booking();
        booking.setBooker(user);
        booking.setEnd(LocalDateTime.parse("1992-05-01T00:30:09"));

        List<Booking> bookingList = new ArrayList<>();
        bookingList.add(booking);

        when(bookingsRepository.getBookingItemBooker(1, 1)).thenReturn(bookingList);
        when(itemRepositoryJpa.findById(1)).thenReturn(Optional.of(item));
        when(userRepositoryJpa.findById(1)).thenReturn(Optional.of(user));
        when(commenRepository.save(comment)).thenReturn(comment);
        when(mappingComment.mapCommentDto(comment)).thenReturn(commentDto);

        Assertions.assertEquals(commentDto, itemServiceImpl.createComment(comment, 1, 1));

    }

    @Test
    public void createCommentErrorCreated() {
        Booking booking = new Booking();
        booking.setEnd(LocalDateTime.parse("2024-05-01T00:30:09"));

        Comments comment = new Comments();
        comment.setText("Text");

        List<Booking> bookingList = new ArrayList<>();
        bookingList.add(booking);

        when(bookingsRepository.getBookingItemBooker(1, 1)).thenReturn(bookingList);

        Assertions.assertThrows(RequestException.class, () -> {
            itemServiceImpl.createComment(comment, 1, 1);
        });
    }


}
