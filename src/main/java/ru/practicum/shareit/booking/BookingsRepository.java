package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookingsRepository extends JpaRepository<Booking, Integer> {

    @Query(value = "select b.id,b.start_date,b.end_date,b.booker_id,b.item_id,b.status " +
            "from bookings as b left join items as i on i.id = b.item_id " +
            "left join users as u on u.id = b.booker_id " +
            "where b.id = ?1", nativeQuery = true)
    Booking getBooking(int bookingId, int bookerId);


    @Query(value = "select * " +
            "from bookings as b left join items as i on i.id = b.item_id " +
            "left join users as u on u.id = b.booker_id " +
            "where b.booker_id = ?1 " +
            "order by b.start_date DESC ", nativeQuery = true)
    List<Booking> getAllBooking(int booker);

    @Query(value = "select * " +
            "from bookings as b left join items as i on i.id = b.item_id " +
            "left join users as u on u.id = b.booker_id " +
            "where i.owner = ?1 " +
            "order by b.start_date DESC ", nativeQuery = true)
    List<Booking> getAllOwnerBooking(int booker);

    @Query(value = "select * " +
            "from bookings as b left join items as i on i.id = b.item_id " +
            "left join users as u on u.id = b.booker_id " +
            "where i.owner = ?1 AND b.status = 'WAITING'" +
            "order by b.start_date DESC ", nativeQuery = true)
    List<Booking> getWaitingOwnerBooking(int booker);

    @Query(value = "select * " +
            "from bookings as b left join items as i on i.id = b.item_id " +
            "left join users as u on u.id = b.booker_id " +
            "where b.booker_id = ?1 AND b.status = 'WAITING'" +
            "order by b.start_date DESC ", nativeQuery = true)
    List<Booking> getWaitingBooking(int booker);

    @Query(value = "select * " +
            "from bookings as b left join items as i on i.id = b.item_id " +
            "left join users as u on u.id = b.booker_id " +
            "where i.owner = ?1 AND b.status = 'REJECTED'" +
            "order by b.start_date DESC ", nativeQuery = true)
    List<Booking> getRejectedOwnerBooking(int booker);

    @Query(value = "select * " +
            "from bookings as b left join items as i on i.id = b.item_id " +
            "left join users as u on u.id = b.booker_id " +
            "where b.booker_id = ?1 AND b.status = 'REJECTED'" +
            "order by b.start_date DESC ", nativeQuery = true)
    List<Booking> getRejectedBooking(int booker);

    @Query(value = "select * " +
            "from bookings as b left join items as i on i.id = b.item_id " +
            "left join users as u on u.id = b.booker_id " +
            "where i.owner = ?1 AND b.start_date>NOW()" +
            "order by b.start_date DESC ", nativeQuery = true)
    List<Booking> getFutureOwnerBooking(int booker);

    @Query(value = "select * " +
            "from bookings as b left join items as i on i.id = b.item_id " +
            "left join users as u on u.id = b.booker_id " +
            "where b.booker_id = ?1 AND b.start_date>NOW()" +
            "order by b.start_date DESC ", nativeQuery = true)
    List<Booking> getFutureBooking(int booker);

    @Query(value = "select * " +
            "from bookings as b left join items as i on i.id = b.item_id " +
            "left join users as u on u.id = b.booker_id " +
            "where b.booker_id = ?1 AND b.item_id = ?2 AND b.status<>'REJECTED'", nativeQuery = true)
    List<Booking> getBookingItemBooker(int booker, int itemId);


    @Query(value = "select * " +
            "from bookings as b left join items as i on i.id = b.item_id " +
            "left join users as u on u.id = b.booker_id " +
            "where i.owner = ?1 AND b.item_id = ?2", nativeQuery = true)
    List<Booking> getBookingItemOwner(int owner, int itemId);

    @Query(value = "select * " +
            "from bookings as b left join items as i on i.id = b.item_id " +
            "left join users as u on u.id = b.booker_id " +
            "where b.booker_id = ?1 AND b.start_date<NOW() AND b.end_date>NOW()" +
            "order by b.start_date DESC ", nativeQuery = true)
    List<Booking> getCurrentBooking(int booker);

    @Query(value = "select * " +
            "from bookings as b left join items as i on i.id = b.item_id " +
            "left join users as u on u.id = b.booker_id " +
            "where i.owner = ?1 AND b.start_date<NOW() AND b.end_date>NOW()" +
            "order by b.start_date DESC ", nativeQuery = true)
    List<Booking> getCurrentOwnerBooking(int booker);

    @Query(value = "select * " +
            "from bookings as b left join items as i on i.id = b.item_id " +
            "left join users as u on u.id = b.booker_id " +
            "where b.booker_id = ?1 AND b.end_date<NOW()" +
            "order by b.start_date DESC ", nativeQuery = true)
    List<Booking> getPastBooking(int booker);

    @Query(value = "select * " +
            "from bookings as b left join items as i on i.id = b.item_id " +
            "left join users as u on u.id = b.booker_id " +
            "where i.owner = ?1 AND b.end_date<NOW()" +
            "order by b.start_date DESC ", nativeQuery = true)
    List<Booking> getPastOwnerBooking(int booker);


    @Query(value = "select * " +
            "from bookings as b left join items as i on i.id = b.item_id " +
            "left join users as u on u.id = b.booker_id " +
            "where b.booker_id = ?1 " +
            "order by b.start_date DESC OFFSET ?2 ROWS FETCH NEXT ?3 ROWS ONLY", nativeQuery = true)
    List<Booking> getAllBookingPagination(int booker, int from, int size);

    @Query(value = "select * " +
            "from bookings as b left join items as i on i.id = b.item_id " +
            "left join users as u on u.id = b.booker_id " +
            "where i.owner = ?1 " +
            "order by b.start_date DESC OFFSET ?2 ROWS FETCH NEXT ?3 ROWS ONLY", nativeQuery = true)
    List<Booking> getAllOwnerBookingPagination(int booker, int from, int size);
}
