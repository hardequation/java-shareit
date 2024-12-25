package ru.practicum.shareit.booking;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dal.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.CreateUserDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Transactional
@SpringBootTest
@TestPropertySource(properties = {"jdbc:h2:mem:shareit"})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingServiceTest {

    @Autowired
    private final EntityManager em;
    UserDto user1;
    UserDto user2;
    ItemDto item1;
    TypedQuery<Booking> queryFindById;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private BookingService bookingService;
    @Autowired
    private UserService userService;
    @Autowired
    private ItemService itemService;

    @BeforeEach
    void setUp() {
        CreateUserDto userDto1 = CreateUserDto.builder()
                .name("Some Name1")
                .email("abc1@g.com")
                .build();

        CreateUserDto userDto2 = CreateUserDto.builder()
                .name("Some Name2")
                .email("abc2@g.com")
                .build();

        user1 = userService.save(userDto1);
        user2 = userService.save(userDto2);

        CreateItemDto itemDto1 = CreateItemDto.builder()
                .name("Item1")
                .description("Some description")
                .available(true)
                .build();

        item1 = itemService.create(user1.getId(), itemDto1);

        queryFindById = em.createQuery("Select b from Booking b where b.id = :id", Booking.class);
    }

    @Test
    void save() {
        CreateBookingDto toCreate = CreateBookingDto.builder()
                .itemId(item1.getId())
                .start(LocalDateTime.now().plusDays(2))
                .end(LocalDateTime.now().plusDays(5))
                .build();

        BookingDto created = bookingService.create(toCreate, user1.getId());
        Booking booking = queryFindById.setParameter("id", created.getId()).getSingleResult();

        assertNotNull(booking);
        assertNotNull(booking.getId());
        assertEquals(created.getItem().getId(), item1.getId());
        assertEquals(created.getBooker().getId(), user1.getId());
        assertEquals(created.getStart(), toCreate.getStart());
        assertEquals(created.getEnd(), toCreate.getEnd());
    }

    @Test
    void process() {
        CreateBookingDto toCreate = CreateBookingDto.builder()
                .itemId(item1.getId())
                .start(LocalDateTime.now().plusDays(2))
                .end(LocalDateTime.now().plusDays(5))
                .build();

        BookingDto created = bookingService.create(toCreate, user1.getId());
        BookingDto processed = bookingService.processBooking(item1.getOwner(), created.getId(), false);

        assertNotNull(processed);
        assertEquals(created.getItem().getId(), item1.getId());
        assertEquals(created.getBooker().getId(), user1.getId());
        assertEquals(created.getStart(), processed.getStart());
        assertEquals(created.getEnd(), processed.getEnd());
        assertEquals(BookingStatus.REJECTED, processed.getStatus());
    }

    @Test
    void findById() {
        CreateBookingDto toCreate = CreateBookingDto.builder()
                .itemId(item1.getId())
                .start(LocalDateTime.now().plusDays(2))
                .end(LocalDateTime.now().plusDays(5))
                .build();

        BookingDto created = bookingService.create(toCreate, user1.getId());
        BookingDto found = bookingService.findById(created.getId(), user1.getId());

        assertNotNull(found);
        assertEquals(created.getItem().getId(), item1.getId());
        assertEquals(created.getBooker().getId(), user1.getId());
        assertEquals(created.getStart(), found.getStart());
        assertEquals(created.getEnd(), found.getEnd());
        assertEquals(BookingStatus.WAITING, found.getStatus());
    }

    @Test
    void findByBookerAndState() {
        CreateBookingDto toCreate1 = CreateBookingDto.builder()
                .itemId(item1.getId())
                .start(LocalDateTime.now().plusDays(2))
                .end(LocalDateTime.now().plusDays(5))
                .build();

        CreateBookingDto toCreate2 = CreateBookingDto.builder()
                .itemId(item1.getId())
                .start(LocalDateTime.now().plusDays(2))
                .end(LocalDateTime.now().plusDays(5))
                .build();

        BookingDto created1 = bookingService.create(toCreate1, user1.getId());
        BookingDto created2 = bookingService.create(toCreate2, user2.getId());
        List<BookingDto> bookings = bookingService.findByBookerAndState(user1.getId(), BookingState.WAITING);

        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        BookingDto found = bookings.getFirst();
        assertEquals(created1.getItem().getId(), item1.getId());
        assertEquals(created1.getBooker().getId(), user1.getId());
        assertEquals(created1.getStart(), found.getStart());
        assertEquals(created1.getEnd(), found.getEnd());
        assertEquals(BookingStatus.WAITING, found.getStatus());
    }

    @Test
    void findByOwnerAndState() {
        CreateBookingDto toCreate1 = CreateBookingDto.builder()
                .itemId(item1.getId())
                .start(LocalDateTime.now().plusDays(2))
                .end(LocalDateTime.now().plusDays(5))
                .build();

        CreateBookingDto toCreate2 = CreateBookingDto.builder()
                .itemId(item1.getId())
                .start(LocalDateTime.now().plusDays(2))
                .end(LocalDateTime.now().plusDays(5))
                .build();

        BookingDto created1 = bookingService.create(toCreate1, user1.getId());
        BookingDto created2 = bookingService.create(toCreate2, user2.getId());
        List<BookingDto> bookings = bookingService.findByBookerAndState(item1.getOwner(), BookingState.WAITING);

        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        BookingDto found = bookings.getFirst();
        assertEquals(created1.getItem().getId(), item1.getId());
        assertEquals(created1.getBooker().getId(), user1.getId());
        assertEquals(created1.getStart(), found.getStart());
        assertEquals(created1.getEnd(), found.getEnd());
        assertEquals(BookingStatus.WAITING, found.getStatus());
    }

}