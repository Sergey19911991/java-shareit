package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.MappingBooking;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.RequestException;
import ru.practicum.shareit.exception.ValidationExeption;
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
        if (bookingsRepository.findById(bookingId).get().getStatus() == Booking.Status.APPROVED) {
            throw new RequestException("Данный запрос на бронирование уже подтвержден");
        }
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
        Booking booking = bookingsRepository.findById(bookingId).orElse(null);
        if (booking != null && userRepositoryJpa.existsById(bookerId) && (booking.booker.getId() == bookerId || booking.item.getOwner() == bookerId)) {
            log.info("Данные о бронировании с id = {}", bookingId);
            return bookingsRepository.getBooking(bookingId, bookerId);
        } else {
            log.error("Такого пользователя не существует и/или пользователь не является хозяином вещи и/или пользователь не забронировал данную вещь");
            throw new NotFoundException("Такого пользователя не существует и/или пользователь не является хозяином вещи и/или пользователь не забронировал данную вещь");
        }

    }

    @Override
    public List<Booking> getAllBooking(String state, int booker, UserType.Type userType, Integer from, Integer size) {
        if (userRepositoryJpa.existsById(booker)) {
            if (state.equals("ALL")) {
                if (userType.equals(UserType.Type.BOOKER)) {
                    log.info("Данные о всех бронированиях пользователя с id = {}", booker);
                    return bookingsRepository.getAllBookingPagination(booker, from, size);
                }
                if (userType.equals(UserType.Type.OWNER)) {
                    log.info("Данные о всех бронированиях всех вещей, владельцем которых является пользователь с id = {}", booker);
                    return bookingsRepository.getAllOwnerBookingPagination(booker, from, size);
                }
            }
            if (state.equals("WAITING")) {
                if (userType == UserType.Type.BOOKER) {
                    log.info("Данные о бронированиях пользователя с id = {}, ожидающих подтверждения", booker);
                    return bookingsRepository.getWaitingBooking(booker);
                }
                if (userType == UserType.Type.OWNER) {
                    log.info("Данные о всех бронированиях,ожидающих подтверждения, вещей, владельцем которых является пользователь с id = {}", booker);
                    return bookingsRepository.getWaitingOwnerBooking(booker);
                }
            }
            if (state.equals("REJECTED")) {
                if (userType.equals(UserType.Type.BOOKER)) {
                    log.info("Данные о бронированиях пользователя с id = {}, отклоненных владельцем вещи", booker);
                    return bookingsRepository.getRejectedBooking(booker);
                }
                if (userType.equals(UserType.Type.OWNER)) {
                    log.info("Данные о всех бронированиях,отклоненных владельцем вещи, вещей, владельцем которых является пользователь с id = {}", booker);
                    return bookingsRepository.getRejectedOwnerBooking(booker);
                }
            }
            if (state.equals("FUTURE")) {
                if (userType == UserType.Type.BOOKER) {
                    log.info("Данные о предстоящих бронированиях пользователя с id = {}", booker);
                    return bookingsRepository.getFutureBooking(booker);
                }
                if (userType == UserType.Type.OWNER) {
                    log.info("Данные о всех предстоящих бронированиях вещей, владельцем которых является пользователь с id = {}", booker);
                    return bookingsRepository.getFutureOwnerBooking(booker);
                }
            }
            if (state.equals("CURRENT")) {
                if (userType.equals(UserType.Type.BOOKER)) {
                    log.info("Данные о текущих бронированиях пользователя с id = {}", booker);
                    return bookingsRepository.getCurrentBooking(booker);
                }
                if (userType.equals(UserType.Type.OWNER)) {
                    log.info("Данные о всех текущих бронированиях вещей, владельцем которых является пользователь с id = {}", booker);
                    return bookingsRepository.getCurrentOwnerBooking(booker);
                }
            }
            if (state.equals("PAST")) {
                if (userType.equals(UserType.Type.BOOKER)) {
                    return bookingsRepository.getPastBooking(booker);
                }
                if (userType.equals(UserType.Type.OWNER)) {
                    return bookingsRepository.getPastOwnerBooking(booker);
                }
            } else {
                throw new ValidationExeption("Unknown state: UNSUPPORTED_STATUS");
            }
            return null;
        } else {
            log.error("Такого пользователя не существует");
            throw new ValidationExeption("Такого пользователя не существует");
        }
    }

    public void validationBooking(Booking booking) {
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
        }
    }


}
