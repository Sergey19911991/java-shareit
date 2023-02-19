package ru.practicum.shareit.item.dto;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;

@Service
public class MappingNextBooking {
    //из Booking в NextBooking
    public NextBooking nextBooking(Booking booking) {
        NextBooking nextBooking = new NextBooking();
        nextBooking.setId(booking.getId());
        nextBooking.setBookerId(booking.getBooker().getId());
        return nextBooking;
    }
}
