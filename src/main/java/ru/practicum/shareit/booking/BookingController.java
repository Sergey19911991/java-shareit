package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingServiceImpl bookingServiceimpl;

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
            return bookingServiceimpl.updateBookingApp(bookingId, bookerId);
        } else {
            return bookingServiceimpl.updateBookingRej(bookingId, bookerId);
        }
    }

    @GetMapping
    public List<Booking> getBookingUser(@RequestParam(value = "state", defaultValue = "ALL") String state, @RequestHeader("X-Sharer-User-Id") int booker, @RequestParam(value = "from", required = false) Integer from, @RequestParam(value = "from", required = false) Integer size) {
        return bookingServiceimpl.getAllBooking(state, booker, UserType.Type.BOOKER, from, size);
    }

    @GetMapping(value = "/owner")
    public List<Booking> getAllBookingOwner(@RequestParam(value = "state", defaultValue = "ALL") String state, @RequestHeader("X-Sharer-User-Id") int owner, @RequestParam(value = "from", required = false) Integer from, @RequestParam(value = "from", required = false) Integer size) {
        return bookingServiceimpl.getAllBooking(state, owner, UserType.Type.OWNER, from, size);
    }
}
