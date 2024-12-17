package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dal.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static ru.practicum.shareit.exception.ErrorMessages.BOOKING_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;

    public Booking save(Booking booking) {
        return bookingRepository.save(booking);
    }

    public Booking findById(Long id) {
        return bookingRepository.findById(id).orElseThrow(() -> new NotFoundException(BOOKING_NOT_FOUND + id));
    }

    public List<Booking> findByBooker(User booker, BookingState state) {
        return bookingRepository.findByBookerAndState(booker, state.toString());
    }

    public List<Booking> findByOwner(User owner, BookingState state) {
        return bookingRepository.findByOwnerAndState(owner, state.toString());
    }
}
