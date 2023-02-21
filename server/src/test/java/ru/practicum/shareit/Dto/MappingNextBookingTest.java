package ru.practicum.shareit.Dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.Booking;

import ru.practicum.shareit.item.dto.MappingNextBooking;
import ru.practicum.shareit.item.dto.NextBooking;
import ru.practicum.shareit.user.User;

public class MappingNextBookingTest {

    private MappingNextBooking mappingNextBooking;


    @BeforeEach
    void setUp() {
        mappingNextBooking = new MappingNextBooking();
    }

    @Test
    public void createNextBooking() {
        User user = new User();
        user.setId(1);

        Booking booking = new Booking();
        booking.setId(1);
        booking.setBooker(user);

        NextBooking nextBooking = new NextBooking();
        nextBooking.setId(1);
        nextBooking.setBookerId(1);

        Assertions.assertEquals(mappingNextBooking.nextBooking(booking), nextBooking);
    }
}
