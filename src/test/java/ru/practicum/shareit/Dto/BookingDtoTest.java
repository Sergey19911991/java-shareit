package ru.practicum.shareit.Dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.MappingBooking;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.item.ItemRepositoryJpa;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.when;

public class BookingDtoTest {
    private ItemRepositoryJpa itemRepositoryJpa;

    private MappingBooking mappingBooking;

    @BeforeEach
    void setUp() {
        itemRepositoryJpa = Mockito.mock(ItemRepositoryJpa.class);
        mappingBooking = new MappingBooking(itemRepositoryJpa);
    }

    @Test
    public void bookingDto() {
        Item item = new Item();

        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(1);
        bookingDto.setEnd(LocalDateTime.parse("1992-05-01T00:30:09"));
        bookingDto.setStart(LocalDateTime.parse("1991-05-01T00:30:09"));

        Booking booking = new Booking();
        booking.setEnd(LocalDateTime.parse("1992-05-01T00:30:09"));
        booking.setStart(LocalDateTime.parse("1991-05-01T00:30:09"));
        booking.setItem(item);

        when(itemRepositoryJpa.existsById(bookingDto.getItemId())).thenReturn(true);
        when(itemRepositoryJpa.findById(bookingDto.getItemId())).thenReturn(Optional.of(item));

        Assertions.assertEquals(booking, mappingBooking.mapItemDto(bookingDto));
    }


    @Test
    public void bookingDtoError() {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(1);

        when(itemRepositoryJpa.existsById(bookingDto.getItemId())).thenReturn(false);

        Assertions.assertThrows(NotFoundException.class, () -> {
            mappingBooking.mapItemDto(bookingDto);
        }, "Такого бронирования не существует");
    }
}
