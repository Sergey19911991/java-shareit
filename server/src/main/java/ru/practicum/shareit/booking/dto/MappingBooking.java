package ru.practicum.shareit.booking.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepositoryJpa;

@RequiredArgsConstructor
@Service
public class MappingBooking {
    private final ItemRepositoryJpa itemRepositoryJpa;

    //из dto
    public Booking mapItemDto(BookingDto bookingDto) {
        Booking booking = new Booking();
        if (itemRepositoryJpa.existsById(bookingDto.getItemId())) {
            booking.setItem(itemRepositoryJpa.findById(bookingDto.getItemId()).get());
            booking.setEnd(bookingDto.getEnd());
            booking.setStart(bookingDto.getStart());
            return booking;
        } else {
            throw new NotFoundException("Такого бронирования не существует");
        }
    }

}
