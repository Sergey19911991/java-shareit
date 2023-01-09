package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.MappingBooking;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.exeption.RequestException;
import ru.practicum.shareit.exeption.ValidationExeption;
import ru.practicum.shareit.item.ItemRepositoryJpa;
import ru.practicum.shareit.user.UserRepositoryJpa;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingsRepository bookingsRepository;

    private final ItemRepositoryJpa itemRepositoryJpa;

    private final UserRepositoryJpa userRepositoryJpa;
    private final MappingBooking mappingBooking;

    @Override
    public Booking creatBooking(BookingDto bookingDto, int bookerId) {
        Booking booking = mappingBooking.mapItemDto(bookingDto);
        booking.setEnd(bookingDto.getEnd());
        booking.setStart(bookingDto.getStart());
        if (bookerId == booking.getItem().getOwner()) {
            log.error("Владелец вещи не может ее забронировать");
            throw new NotFoundException("Владелец вещи не может ее забронировать");
        }
        if (userRepositoryJpa.existsById(bookerId)) {
            validationBooking(booking);
            booking.setStatus(Booking.Status.WAITING);
            booking.setBooker(userRepositoryJpa.findById(bookerId).get());
            log.info("Создано бронирование");
            return bookingsRepository.save(booking);
        } else {
            log.error("Пользователь с таким id не найден");
            throw new ValidationExeption("Пользователь с таким id не найден");
        }
    }

    @Override
    public Booking updateBookingApp(int bookingId, int booker) {
        if (bookingsRepository.findById(bookingId).get().item.getOwner() == booker) {
            Booking booking = bookingsRepository.findById(bookingId).get();
            booking.setStatus(Booking.Status.APPROVED);
            log.info("Запрос на бронирование подтвержден владельцем вещи");
            return bookingsRepository.save(booking);
        } else {
            log.error("Только владелец вещи может подтвердить запрос на ее бронирование!");
            throw new NotFoundException("Только владелец вещи может подтвердить запрос на ее бронирование!");
        }
    }

    @Override
    public Booking updateBookingRej(int bookingId, int booker) {
        if (bookingsRepository.findById(bookingId).get().item.getOwner() == booker) {
            Booking booking = bookingsRepository.findById(bookingId).get();
            booking.setStatus(Booking.Status.REJECTED);
            log.info("Запрос на бронирование вещи отклонен");
            return bookingsRepository.save(booking);
        } else {
            log.error("Только владелец вещи может отклонить запрос на ее бронирование!");
            throw new RequestException("Только владелец вещи может отклонить запрос на ее бронирование!");
        }
    }

    @Override
    public Booking getBooking(int bookingId, int bookerId) {
        if (bookingsRepository.existsById(bookingId) && userRepositoryJpa.existsById(bookerId) && (bookingsRepository.findById(bookingId).get().booker.getId() == bookerId || bookingsRepository.findById(bookingId).get().item.getOwner() == bookerId)) {
            log.info("Данные о бронировании с id = {}", bookingId);
            return bookingsRepository.getBooking(bookingId, bookerId);
        } else {
            log.error("Такого бронирования не существует и/или такого пользователя не существует и/или пользователь не является хозяином вещи и/или пользователь не забронировал данную вещь");
            throw new NotFoundException("Такого бронирования не существует и/или такого пользователя не существует и/или пользователь не является хозяином вещи и/или пользователь не забронировал данную вещь");
        }
    }

    @Override
    public List<Booking> getAllBooking(String state, int booker) {
        if (userRepositoryJpa.existsById(booker)) {
            log.info("Данные о всех бронированиях пользователя с id = {}", booker);
            return bookingsRepository.getAllBooking(booker, state);
        } else {
            log.error("Такого пользователя не существует");
            throw new ValidationExeption("Такого пользователя не существует");
        }
    }

    @Override
    public List<Booking> getAllBookingOwner(String state, int owner) {
        if (userRepositoryJpa.existsById(owner)) {
            log.info("Данные о всех бронированиях всех вещей, владельцем которых является пользователь с id = {}", owner);
            return bookingsRepository.getAllOwnerBooking(owner, state);
        } else {
            log.error("Такого пользователя не существует");
            throw new ValidationExeption("Такого пользователя не существует");
        }
    }

    private void validationBooking(Booking booking) {
        if (!itemRepositoryJpa.existsById(booking.item.getId())) {
            log.error("Вещи с таким id не существует");
            throw new NotFoundException("Вещи с таким id не существует");
        } else {
            if (itemRepositoryJpa.findById(booking.item.getId()).get().getAvailable() == false) {
                log.error("Данная вещь не доступна для бронирования");
                throw new RequestException("Данная вещь не доступна для бронирования");
            }
            if (booking.getStart().isAfter(booking.getEnd())) {
                log.error("Начало бронирования не может быть позже окончания бронирования");
                throw new RequestException("Начало бронирования не может быть позже окончания бронирования");
            }
            if (booking.getStart().isBefore(LocalDateTime.now())) {
                log.error("Начало бронирования не может быть раньше настоящего момента времени");
                throw new RequestException("Начало бронирования не может быть раньше настоящего момента времени");
            }
            if (booking.getEnd().isBefore(LocalDateTime.now())) {
                log.error("Окончание бронирования не может быть раньше настоящего момента времени");
                throw new RequestException("Окончание бронирования не может быть раньше настоящего момента времени");
            }
        }
    }
}
