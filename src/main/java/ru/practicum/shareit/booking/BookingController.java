package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exeption.RequestException;
import ru.practicum.shareit.exeption.ValidationExeption;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingServiceImpl bookingServiceimpl;
    private final BookingsRepository bookingsRepository;

    @PostMapping
    public Booking creatBooking(@RequestBody BookingDto bookinng, @RequestHeader("X-Sharer-User-Id") int bookerId) {
        return bookingServiceimpl.creatBooking(bookinng, bookerId);
    }

    @GetMapping(value = "/{bookingId}")
    public Booking getBooking(@PathVariable("bookingId") Integer bookingId, @RequestHeader("X-Sharer-User-Id") int bookerId) {
        return bookingServiceimpl.getBooking(bookingId, bookerId);
    }

    @PatchMapping(value = "/{bookingId}")
    public Booking updateBooking(@PathVariable("bookingId") int bookingId, @RequestParam String approved, @RequestHeader("X-Sharer-User-Id") int bookerId) {
        if (approved.equals("true")) {
            if (bookingsRepository.findById(bookingId).get().getStatus() != Booking.Status.APPROVED) {
                return bookingServiceimpl.updateBookingApp(bookingId, bookerId);
            } else {
                throw new RequestException("Данный запрос на бронирование уже подтвержден");
            }
        } else {
            return bookingServiceimpl.updateBookingRej(bookingId, bookerId);
        }

    }

    @GetMapping
    public List<Booking> getBookingUser(@RequestParam(value = "state", defaultValue = "ALL") String state, @RequestHeader("X-Sharer-User-Id") int booker) {
        if (state.equals("ALL")) {
            return bookingServiceimpl.getAllBooking(state, booker);
        }
        if (state.equals("WAITING")) {
            return bookingsRepository.getWaitingBooking(booker, state);
        }
        if (state.equals("REJECTED")) {
            return bookingsRepository.getRejectedBooking(booker, state);
        }
        if (state.equals("FUTURE")) {
            return bookingsRepository.getFutureBooking(booker, state);
        }
        if (state.equals("CURRENT")) {
            return bookingsRepository.getCurrentBooking(booker, state);
        }
        if (state.equals("PAST")) {
            return bookingsRepository.getPastBooking(booker, state);
        } else {
            throw new ValidationExeption("Unknown state: UNSUPPORTED_STATUS");
        }
    }

    @GetMapping(value = "/owner")
    public List<Booking> getAllBookingOwner(@RequestParam(value = "state", defaultValue = "ALL") String state, @RequestHeader("X-Sharer-User-Id") int owner) {
        if (state.equals("ALL")) {
            return bookingServiceimpl.getAllBookingOwner(state, owner);
        }
        if (state.equals("WAITING")) {
            return bookingsRepository.getWaitingOwnerBooking(owner, state);
        }
        if (state.equals("REJECTED")) {
            return bookingsRepository.getRejectedOwnerBooking(owner, state);
        }
        if (state.equals("FUTURE")) {
            return bookingsRepository.getFutureOwnerBooking(owner, state);
        }
        if (state.equals("CURRENT")) {
            return bookingsRepository.getCurrentOwnerBooking(owner, state);
        }
        if (state.equals("PAST")) {
            return bookingsRepository.getPastOwnerBooking(owner, state);
        } else {
            throw new ValidationExeption("Unknown state: UNSUPPORTED_STATUS");
        }
    }
}
