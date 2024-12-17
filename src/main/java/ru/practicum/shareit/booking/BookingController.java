package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.AuthentificationException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.WrongRequirementsException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

import static ru.practicum.shareit.Constants.HEADER_USER_PARAMETER;
import static ru.practicum.shareit.exception.ErrorMessages.ITEM_NOT_FOUND;
import static ru.practicum.shareit.exception.ErrorMessages.USER_NOT_FOUND;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingMapper bookingMapper;

    private final BookingService bookingService;

    private final ItemService itemService;

    private final UserService userService;

    @PostMapping
    BookingDto create(@RequestHeader(HEADER_USER_PARAMETER) Long bookerId,
                    @Valid @RequestBody CreateBookingDto dto) {
        if (!dto.getStart().isBefore(dto.getEnd())) {
            throw new WrongRequirementsException("Start date of booking should be before end date");
        }

        User booker = userService.findById(bookerId).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND + bookerId));

        Long itemId = dto.getItemId();
        Item item = itemService.findById(itemId).orElseThrow(() -> new NotFoundException(ITEM_NOT_FOUND + itemId));

        if (item.getAvailable().equals(false)) {
            throw new WrongRequirementsException("Unable to book unavailable item: " + itemId);
        }

        Booking toCreate = bookingMapper.map(dto, booker, item);
        Booking createdBooking = bookingService.save(toCreate);
        return bookingMapper.map(createdBooking);
    }

    @PatchMapping("/{bookingId}")
    BookingDto processBooking(@RequestHeader(HEADER_USER_PARAMETER) Long userId,
                              @PathVariable @NotNull Long bookingId,
                              @RequestParam @NotNull Boolean approved) {
        Booking booking = bookingService.findById(bookingId);
        Item item = booking.getItem();

        Long itemOwner = item.getOwner().getId();
        if (!itemOwner.equals(userId)) {
            throw new AuthentificationException("Only owner of item can approve/reject booking of this item");
        }

        if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }
        bookingService.save(booking);
        return bookingMapper.map(booking);
    }

    @GetMapping("/{bookingId}")
    BookingDto findById(@RequestHeader(HEADER_USER_PARAMETER) Long userId,
                        @PathVariable Long bookingId) {
        Booking booking = bookingService.findById(bookingId);
        Item item = booking.getItem();
        Long ownerId = item.getOwner().getId();

        if (!userId.equals(booking.getBooker().getId()) && !userId.equals(ownerId)) {
            throw new AuthentificationException("Only booker and owner of item can look at booking of this item");
        }

        return bookingMapper.map(booking);
    }

    @GetMapping
    List<BookingDto> findByUserAndState(@RequestHeader(HEADER_USER_PARAMETER) Long userId,
                                        @RequestParam(defaultValue = "ALL") BookingState state) {
        User user = userService.findById(userId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND + userId));
        List<Booking> bookings = bookingService.findByBooker(user, state);
        List<BookingDto> bookingDtoList = new ArrayList<>();

        for (Booking booking: bookings) {
            bookingDtoList.add(bookingMapper.map(booking));
        }
        return bookingDtoList;
    }

    @GetMapping("/owner")
    List<BookingDto> findByOwnerAndState(@RequestHeader(HEADER_USER_PARAMETER) Long ownerId,
                                         @RequestParam(defaultValue = "ALL") BookingState state) {
        User owner = userService.findById(ownerId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND + ownerId));
        List<Booking> bookings = bookingService.findByOwner(owner, state);
        List<BookingDto> bookingDtoList = new ArrayList<>();

        for (Booking booking: bookings) {
            bookingDtoList.add(bookingMapper.map(booking));
        }
        return bookingDtoList;
    }
}
