package ru.practicum.shareit.booking.dal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Booking save(Booking booking);

    Optional<Booking> findById(Long id);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.booker = :booker " +
            "AND (:state = 'ALL' OR " +
            "    (:state = 'CURRENT' AND CURRENT_TIMESTAMP BETWEEN b.start AND b.end) OR " +
            "    (:state = 'PAST' AND CURRENT_TIMESTAMP > b.end) OR " +
            "    (:state = 'FUTURE' AND CURRENT_TIMESTAMP < b.start) OR " +
            "    (:state = 'WAITING' AND b.status = ru.practicum.shareit.booking.model.BookingStatus.WAITING) OR " +
            "    (:state = 'REJECTED' AND b.status = ru.practicum.shareit.booking.model.BookingStatus.REJECTED)) " +
            "ORDER BY b.start ASC")
    List<Booking> findByBookerAndState(@Param("booker") User booker,
                                       @Param("state") String state);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.booker = :booker " +
            "AND b.item = :item " +
            "AND b.status = :status " +
            "AND b.end < CURRENT_TIMESTAMP " +
            "ORDER BY b.start ASC")
    List<Booking> findByBookerAndItemAndStatus(@Param("booker") User booker,
                                               @Param("item") Item item,
                                               @Param("status") BookingStatus status);

    @Query("SELECT b FROM Booking b " +
            "JOIN Item i ON b.item.id = i.id " +
            "WHERE i.owner = :owner " +
            "AND (:state = 'ALL' OR " +
            "    (:state = 'CURRENT' AND CURRENT_TIMESTAMP BETWEEN b.start AND b.end) OR " +
            "    (:state = 'PAST' AND CURRENT_TIMESTAMP > b.end) OR " +
            "    (:state = 'FUTURE' AND CURRENT_TIMESTAMP < b.start) OR " +
            "    (:state = 'WAITING' AND b.status = ru.practicum.shareit.booking.model.BookingStatus.WAITING) OR " +
            "    (:state = 'REJECTED' AND b.status = ru.practicum.shareit.booking.model.BookingStatus.REJECTED)) " +
            "ORDER BY b.start ASC")
    List<Booking> findByOwnerAndState(@Param("owner") User owner,
                                      @Param("state") String state);

    void deleteById(Long id);
}
