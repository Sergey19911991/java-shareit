package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {
    Booking creatBooking(BookingDto bookingDto, int bookerId);

    Booking getBooking(int bookingId, int bookerId);

    Booking updateBookingApp(int bookingId, int booker);

    Booking updateBookingRej(int bookingId, int booker);

    List<Booking> getAllBooking(String state, int booker, UserType.Type userType, Integer from, Integer size);

}
