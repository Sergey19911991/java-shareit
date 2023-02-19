package ru.practicum.shareit.ServiceTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingServiceImpl;
import ru.practicum.shareit.booking.BookingsRepository;
import ru.practicum.shareit.booking.UserType;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.MappingBooking;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.RequestException;
import ru.practicum.shareit.exception.ValidationExeption;
import ru.practicum.shareit.item.ItemRepositoryJpa;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepositoryJpa;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    private BookingsRepository bookingsRepository;

    private ItemRepositoryJpa itemRepositoryJpa;

    private UserRepositoryJpa userRepositoryJpa;
    private MappingBooking mappingBooking;
    private BookingServiceImpl bookingService;

    @BeforeEach
    void setUp() {
        bookingsRepository = Mockito.mock(BookingsRepository.class);
        mappingBooking = Mockito.mock(MappingBooking.class);
        itemRepositoryJpa = Mockito.mock(ItemRepositoryJpa.class);
        userRepositoryJpa = Mockito.mock(UserRepositoryJpa.class);
        bookingService = new BookingServiceImpl(bookingsRepository, itemRepositoryJpa, userRepositoryJpa, mappingBooking);
    }

    @Test
    public void createBooking() {
        Item item = new Item();
        item.setOwner(1);
        item.setId(1);
        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(1);
        bookingDto.setEnd(LocalDateTime.parse("1992-05-01T00:30:09"));
        bookingDto.setStart(LocalDateTime.parse("1991-05-01T00:30:09"));
        Booking booking = new Booking();
        booking.setId(1);
        booking.setItem(item);

        when(mappingBooking.mapItemDto(bookingDto)).thenReturn(booking);

        Assertions.assertThrows(NotFoundException.class, () -> {
            bookingService.creatBooking(bookingDto, 1);
        });
    }

    @Test
    public void createBooking1() {
        Item item = new Item();
        item.setOwner(2);
        item.setId(1);
        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(1);
        bookingDto.setEnd(LocalDateTime.parse("1992-05-01T00:30:09"));
        bookingDto.setStart(LocalDateTime.parse("1991-05-01T00:30:09"));
        Booking booking = new Booking();
        booking.setId(1);
        booking.setItem(item);

        when(mappingBooking.mapItemDto(bookingDto)).thenReturn(booking);
        when(userRepositoryJpa.existsById(1)).thenReturn(false);

        Assertions.assertThrows(ValidationExeption.class, () -> {
            bookingService.creatBooking(bookingDto, 1);
        });
    }

    @Test
    public void createBooking2() {
        User user = new User();
        user.setId(1);

        Item item = new Item();
        item.setOwner(2);
        item.setId(1);
        item.setAvailable(true);

        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(1);
        bookingDto.setEnd(LocalDateTime.parse("2024-05-01T00:30:09"));
        bookingDto.setStart(LocalDateTime.parse("2023-05-01T00:30:09"));

        Booking booking = new Booking();
        booking.setId(1);
        booking.setItem(item);
        booking.setStatus(Booking.Status.WAITING);
        booking.setStart(LocalDateTime.parse("1992-05-01T00:30:09"));
        booking.setEnd(LocalDateTime.parse("1991-05-01T00:30:09"));

        when(mappingBooking.mapItemDto(bookingDto)).thenReturn(booking);
        when(userRepositoryJpa.existsById(1)).thenReturn(true);
        when(bookingsRepository.save(booking)).thenReturn(booking);
        when(itemRepositoryJpa.existsById(1)).thenReturn(true);
        when(itemRepositoryJpa.findById(1)).thenReturn(Optional.of(item));
        when(userRepositoryJpa.findById(1)).thenReturn(Optional.of(user));

        Assertions.assertEquals(booking, bookingService.creatBooking(bookingDto, 1));
    }

    @Test
    public void validationBookingTest() {
        Booking booking = new Booking();
        Item item = new Item();
        item.setId(1);
        booking.setItem(item);

        when(itemRepositoryJpa.existsById(1)).thenReturn(false);

        Assertions.assertThrows(NotFoundException.class, () -> {
            bookingService.validationBooking(booking);
        });
    }

    @Test
    public void validationBookingTest1() {
        Booking booking = new Booking();
        Item item = new Item();
        item.setId(1);
        item.setAvailable(false);
        booking.setItem(item);

        when(itemRepositoryJpa.existsById(1)).thenReturn(true);
        when(itemRepositoryJpa.findById(1)).thenReturn(Optional.of(item));

        Assertions.assertThrows(RequestException.class, () -> {
            bookingService.validationBooking(booking);
        });
    }


    @Test
    public void validationBookingTest2() {
        Booking booking = new Booking();
        Item item = new Item();
        item.setId(1);
        item.setAvailable(true);
        booking.setItem(item);
        booking.setStart(LocalDateTime.parse("1993-05-01T00:30:09"));
        booking.setEnd(LocalDateTime.parse("1992-05-01T00:30:09"));

        when(itemRepositoryJpa.existsById(1)).thenReturn(true);
        when(itemRepositoryJpa.findById(1)).thenReturn(Optional.of(item));

        Assertions.assertThrows(RequestException.class, () -> {
            bookingService.validationBooking(booking);
        });
    }

    @Test
    public void validationBookingTest3() {
        Booking booking = new Booking();
        Item item = new Item();
        item.setId(1);
        item.setAvailable(true);
        booking.setItem(item);
        booking.setStart(LocalDateTime.parse("1991-05-01T00:30:09"));
        booking.setEnd(LocalDateTime.parse("2024-05-01T00:30:09"));

        when(itemRepositoryJpa.existsById(1)).thenReturn(true);
        when(itemRepositoryJpa.findById(1)).thenReturn(Optional.of(item));

        Assertions.assertThrows(RequestException.class, () -> {
            bookingService.validationBooking(booking);
        });
    }



    @Test
    public void getAll2() {
        List<Booking> list = new ArrayList<>();

        when(userRepositoryJpa.existsById(1)).thenReturn(true);
        when(bookingsRepository.getAllOwnerBookingPagination(1, 0, 1)).thenReturn(list);

        Assertions.assertEquals(list, bookingService.getAllBooking("ALL", 1, UserType.Type.OWNER, 0, 1));
    }

    @Test
    public void getAll3() {
        List<Booking> list = new ArrayList<>();

        when(userRepositoryJpa.existsById(1)).thenReturn(true);
        when(bookingsRepository.getAllBookingPagination(1, 0, 1)).thenReturn(list);

        Assertions.assertEquals(list, bookingService.getAllBooking("ALL", 1, UserType.Type.BOOKER, 0, 1));
    }


    @Test
    public void getAll5() {

        when(userRepositoryJpa.existsById(1)).thenReturn(false);

        Assertions.assertThrows(ValidationExeption.class, () -> {
            bookingService.getAllBooking("ALL", 1, UserType.Type.BOOKER, 0, 1);
        });
    }

    @Test
    public void getAll6() {
        List<Booking> list = new ArrayList<>();

        when(userRepositoryJpa.existsById(1)).thenReturn(true);
        when(bookingsRepository.getWaitingBooking(1)).thenReturn(list);

        Assertions.assertEquals(list, bookingService.getAllBooking("WAITING", 1, UserType.Type.BOOKER, null, null));
    }

    @Test
    public void getAll7() {
        List<Booking> list = new ArrayList<>();

        when(userRepositoryJpa.existsById(1)).thenReturn(true);
        when(bookingsRepository.getWaitingOwnerBooking(1)).thenReturn(list);

        Assertions.assertEquals(list, bookingService.getAllBooking("WAITING", 1, UserType.Type.OWNER, null, null));
    }

    @Test
    public void getAll8() {
        List<Booking> list = new ArrayList<>();

        when(userRepositoryJpa.existsById(1)).thenReturn(true);
        when(bookingsRepository.getRejectedBooking(1)).thenReturn(list);

        Assertions.assertEquals(list, bookingService.getAllBooking("REJECTED", 1, UserType.Type.BOOKER, null, null));
    }

    @Test
    public void getAll9() {
        List<Booking> list = new ArrayList<>();

        when(userRepositoryJpa.existsById(1)).thenReturn(true);
        when(bookingsRepository.getRejectedOwnerBooking(1)).thenReturn(list);

        Assertions.assertEquals(list, bookingService.getAllBooking("REJECTED", 1, UserType.Type.OWNER, null, null));
    }

    @Test
    public void getAll10() {
        List<Booking> list = new ArrayList<>();

        when(userRepositoryJpa.existsById(1)).thenReturn(true);
        when(bookingsRepository.getFutureBooking(1)).thenReturn(list);

        Assertions.assertEquals(list, bookingService.getAllBooking("FUTURE", 1, UserType.Type.BOOKER, null, null));
    }

    @Test
    public void getAll11() {
        List<Booking> list = new ArrayList<>();

        when(userRepositoryJpa.existsById(1)).thenReturn(true);
        when(bookingsRepository.getFutureOwnerBooking(1)).thenReturn(list);

        Assertions.assertEquals(list, bookingService.getAllBooking("FUTURE", 1, UserType.Type.OWNER, null, null));
    }

    @Test
    public void getAll12() {
        List<Booking> list = new ArrayList<>();

        when(userRepositoryJpa.existsById(1)).thenReturn(true);
        when(bookingsRepository.getPastBooking(1)).thenReturn(list);

        Assertions.assertEquals(list, bookingService.getAllBooking("PAST", 1, UserType.Type.BOOKER, null, null));
    }

    @Test
    public void getAll13() {
        List<Booking> list = new ArrayList<>();

        when(userRepositoryJpa.existsById(1)).thenReturn(true);
        when(bookingsRepository.getPastOwnerBooking(1)).thenReturn(list);

        Assertions.assertEquals(list, bookingService.getAllBooking("PAST", 1, UserType.Type.OWNER, null, null));
    }

    @Test
    public void getAll14() {
        List<Booking> list = new ArrayList<>();

        when(userRepositoryJpa.existsById(1)).thenReturn(true);

        Assertions.assertThrows(ValidationExeption.class, () -> {
            bookingService.getAllBooking("XXXX", 1, UserType.Type.BOOKER, 0, 1);
        });

    }

    @Test
    public void getAll15() {
        List<Booking> list = new ArrayList<>();

        when(userRepositoryJpa.existsById(1)).thenReturn(true);
        when(bookingsRepository.getCurrentBooking(1)).thenReturn(list);

        Assertions.assertEquals(list, bookingService.getAllBooking("CURRENT", 1, UserType.Type.BOOKER, null, null));
    }

    @Test
    public void getAll16() {
        List<Booking> list = new ArrayList<>();

        when(userRepositoryJpa.existsById(1)).thenReturn(true);
        when(bookingsRepository.getCurrentOwnerBooking(1)).thenReturn(list);

        Assertions.assertEquals(list, bookingService.getAllBooking("CURRENT", 1, UserType.Type.OWNER, null, null));
    }

    @Test
    public void getBooking() {
        Booking booking = new Booking();
        booking = null;

        when(bookingsRepository.findById(1)).thenReturn(Optional.ofNullable(booking));

        Assertions.assertThrows(NotFoundException.class, () -> {
            bookingService.getBooking(1, 1);
        });

    }

    @Test
    public void getBooking1() {
        User user = new User();
        user.setId(1);

        User user1 = new User();
        user1.setId(2);

        Item item = new Item();
        item.setOwner(2);

        Booking booking = new Booking();
        booking.setBooker(user);
        booking.setItem(item);

        when(bookingsRepository.findById(1)).thenReturn(Optional.ofNullable(booking));
        when(userRepositoryJpa.existsById(1)).thenReturn(true);
        when(bookingsRepository.getBooking(1, 1)).thenReturn(booking);

        Assertions.assertEquals(booking, bookingService.getBooking(1, 1));

    }

    @Test
    public void updateBookingAppTest() {
        Booking booking = new Booking();
        booking.setStatus(Booking.Status.APPROVED);

        when(bookingsRepository.findById(1)).thenReturn(Optional.of(booking));

        Assertions.assertThrows(RequestException.class, () -> {
            bookingService.updateBookingApp(1, 1);
        });
    }

    @Test
    public void updateBookingAppTest1() {
        Item item = new Item();
        item.setOwner(1);

        Booking booking = new Booking();
        booking.setStatus(Booking.Status.WAITING);
        booking.setItem(item);

        Booking booking1 = new Booking();
        booking1.setStatus(Booking.Status.APPROVED);

        when(bookingsRepository.findById(1)).thenReturn(Optional.of(booking));
        when(bookingsRepository.save(booking)).thenReturn(booking1);

        Assertions.assertEquals(booking1, bookingService.updateBookingApp(1, 1));
    }

    @Test
    public void updateBookingAppTest2() {
        Item item = new Item();
        item.setOwner(2);

        Booking booking = new Booking();
        booking.setStatus(Booking.Status.WAITING);
        booking.setItem(item);

        Booking booking1 = new Booking();
        booking1.setStatus(Booking.Status.APPROVED);

        when(bookingsRepository.findById(1)).thenReturn(Optional.of(booking));

        Assertions.assertThrows(NotFoundException.class, () -> {
            bookingService.updateBookingApp(1, 1);
        });
    }

    @Test
    public void updateBookingRej() {
        Item item = new Item();
        item.setOwner(2);

        Booking booking = new Booking();
        booking.setItem(item);

        when(bookingsRepository.findById(1)).thenReturn(Optional.of(booking));

        Assertions.assertThrows(RequestException.class, () -> {
            bookingService.updateBookingRej(1, 1);
        });
    }

    @Test
    public void updateBookingRej1() {
        Item item = new Item();
        item.setOwner(1);

        Booking booking = new Booking();
        booking.setItem(item);

        Booking booking1 = new Booking();
        booking1.setStatus(Booking.Status.REJECTED);


        when(bookingsRepository.findById(1)).thenReturn(Optional.of(booking));
        when(bookingsRepository.save(booking)).thenReturn(booking1);

        Assertions.assertEquals(booking1, bookingService.updateBookingRej(1, 1));
    }
}
