package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.exception.RequestException;
import ru.practicum.shareit.exception.ValidationExeption;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
	private final BookingClient bookingClient;

	@GetMapping
	public ResponseEntity<Object> getBookings(@RequestHeader("X-Sharer-User-Id") long userId,
			@RequestParam(value = "state", defaultValue = "ALL") String stateParam,
			 @RequestParam(value = "from", defaultValue="0") Integer from,
			 @RequestParam(value = "size", defaultValue="10") Integer size) {
		if (from<0||size<=0){
			throw new RequestException("Длина списка не может быть меньше нуля или равна нулю");
		}
		BookingState state = BookingState.from(stateParam)
				.orElseThrow(() -> new ValidationExeption("Unknown state: UNSUPPORTED_STATUS"));
		log.info("Get booking with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
		return bookingClient.getBookings(userId, state, from, size);
	}

	@PostMapping
	public ResponseEntity<Object> bookItem(@RequestHeader("X-Sharer-User-Id") long userId,
			@RequestBody @Valid BookItemRequestDto requestDto) {
		log.info("Creating booking {}, userId={}", requestDto, userId);
		return bookingClient.bookItem(userId, requestDto);
	}

	@GetMapping("/{bookingId}")
	public ResponseEntity<Object> getBooking(@RequestHeader("X-Sharer-User-Id") long userId,
			@PathVariable("bookingId") Long bookingId) {
		log.info("Get booking {}, userId={}", bookingId, userId);
		return bookingClient.getBooking(userId, bookingId);
	}

	@PatchMapping(value="/{bookingId}")
	public ResponseEntity<Object> patchBooking(@RequestHeader("X-Sharer-User-Id") int userId,
											   @RequestParam (name = "approved") String approved,
											   @PathVariable("bookingId") int id) {
		return bookingClient.patchBooking(userId,id,approved);
	}

	@GetMapping("/owner")
	public ResponseEntity<Object> getBookingsOwner(@RequestHeader("X-Sharer-User-Id") long userId,
												   @RequestParam(value = "state", defaultValue = "ALL") String stateParam,
												    @RequestParam(value = "from", defaultValue="0") Integer from,
												   @RequestParam(value = "size", defaultValue="10") Integer size) {
		if (from<0||size<=0){
			throw new RequestException("Длина списка не может быть меньше нуля или равна нулю");
		}
		BookingState state = BookingState.from(stateParam)
				.orElseThrow(() -> new ValidationExeption("Unknown state: UNSUPPORTED_STATUS"));
		return bookingClient.getBookingsOwner(userId,state,from,size);
	}
}
