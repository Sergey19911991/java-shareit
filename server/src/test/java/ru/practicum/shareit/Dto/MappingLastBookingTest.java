package ru.practicum.shareit.Dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.item.dto.LastBooking;
import ru.practicum.shareit.item.dto.MappingLastBooking;
import ru.practicum.shareit.user.User;

public class MappingLastBookingTest {

    private MappingLastBooking mappingLastBooking;

    @BeforeEach
    void setUp() {
        mappingLastBooking = new MappingLastBooking();
    }

    @Test
    public void createLastBooking() {
        User user = new User();
        user.setId(1);

        Booking booking = new Booking();
        booking.setId(1);
        booking.setBooker(user);

        LastBooking lastBooking = new LastBooking();
        lastBooking.setId(1);
        lastBooking.setBookerId(1);

        Assertions.assertEquals(mappingLastBooking.lastBooking(booking), lastBooking);


    }
}
