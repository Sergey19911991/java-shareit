package ru.practicum.shareit.item.dto;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;

@Service
public class MappingLastBooking {
    //из Booking в LastBooking
    public LastBooking lastBooking(Booking booking) {
        LastBooking lastBooking = new LastBooking();
        lastBooking.setId(booking.getId());
        lastBooking.setBookerId(booking.getBooker().getId());
        return lastBooking;
    }
}
