package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class BookingControllerTest {

    @Autowired
    private MockMvc mvc;

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingController bookingController;

    private ObjectMapper objectMapper;

    private CreateBookingDto createDto;
    private BookingDto resultDto;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.findAndRegisterModules();

        mvc = MockMvcBuilders.standaloneSetup(bookingController).build();

        // Создание тестовых объектов
        createDto = CreateBookingDto.builder()
                .start(LocalDateTime.of(2024, 10, 1, 12, 0))
                .end(LocalDateTime.of(2024, 10, 10, 12, 0))
                .itemId(1L)
                .build();

        resultDto = BookingDto.builder()
                .id(1L)
                .booker(User.builder().id(2L).build())
                .start(LocalDateTime.of(2024, 10, 1, 12, 0))
                .end(LocalDateTime.of(2024, 10, 10, 12, 0))
                .item(Item.builder().id(1L).name("Test Item").build())
                .status(BookingStatus.APPROVED)
                .build();
    }

    @Test
    void createBooking() throws Exception {
        when(bookingService.save(any(CreateBookingDto.class), eq(2L)))
                .thenReturn(resultDto);

        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 2L)
                        .content(objectMapper.writeValueAsString(createDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(resultDto.getId()))
                .andExpect(jsonPath("$.booker.id").value(resultDto.getBooker().getId()))
                .andExpect(jsonPath("$.start").value(resultDto.getStartFormatted()))
                .andExpect(jsonPath("$.end").value(resultDto.getEndFormatted()))
                .andExpect(jsonPath("$.status").value(resultDto.getStatus().name()))
                .andExpect(jsonPath("$.item.id").value(resultDto.getItem().getId()))
                .andExpect(jsonPath("$.item.name").value(resultDto.getItem().getName()));
    }

    @Test
    void processBooking() throws Exception {
        when(bookingService.processBooking(eq(2L), eq(1L), eq(true)))
                .thenReturn(resultDto);

        mvc.perform(patch("/bookings/1")
                        .header("X-Sharer-User-Id", 2L)
                        .param("approved", "true")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(resultDto.getId()))
                .andExpect(jsonPath("$.status").value(resultDto.getStatus().name()));
    }

    @Test
    void findById() throws Exception {
        when(bookingService.findById(eq(1L), eq(2L)))
                .thenReturn(resultDto);

        mvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", 2L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(resultDto.getId()))
                .andExpect(jsonPath("$.booker.id").value(resultDto.getBooker().getId()))
                .andExpect(jsonPath("$.start").value(resultDto.getStartFormatted()))
                .andExpect(jsonPath("$.end").value(resultDto.getEndFormatted()))
                .andExpect(jsonPath("$.status").value(resultDto.getStatus().name()))
                .andExpect(jsonPath("$.item.id").value(resultDto.getItem().getId()))
                .andExpect(jsonPath("$.item.name").value(resultDto.getItem().getName()));
    }

    @Test
    void findByBookerAndState() throws Exception {
        when(bookingService.findByBookerAndState(eq(2L), eq(BookingState.ALL)))
                .thenReturn(Collections.singletonList(resultDto));

        mvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 2L)
                        .param("state", "ALL")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(resultDto.getId()))
                .andExpect(jsonPath("$[0].booker.id").value(resultDto.getBooker().getId()))
                .andExpect(jsonPath("$[0].start").value(resultDto.getStartFormatted()))
                .andExpect(jsonPath("$[0].end").value(resultDto.getEndFormatted()))
                .andExpect(jsonPath("$[0].status").value(resultDto.getStatus().name()))
                .andExpect(jsonPath("$[0].item.id").value(resultDto.getItem().getId()))
                .andExpect(jsonPath("$[0].item.name").value(resultDto.getItem().getName()));
    }

    @Test
    void findByOwnerAndState() throws Exception {
        when(bookingService.findByOwnerAndState(eq(2L), eq(BookingState.ALL)))
                .thenReturn(Collections.singletonList(resultDto));

        mvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 2L)
                        .param("state", "ALL")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(resultDto.getId()))
                .andExpect(jsonPath("$[0].booker.id").value(resultDto.getBooker().getId()))
                .andExpect(jsonPath("$[0].start").value(resultDto.getStartFormatted().toString()))
                .andExpect(jsonPath("$[0].end").value(resultDto.getEndFormatted().toString()))
                .andExpect(jsonPath("$[0].status").value(resultDto.getStatus().name()))
                .andExpect(jsonPath("$[0].item.id").value(resultDto.getItem().getId()))
                .andExpect(jsonPath("$[0].item.name").value(resultDto.getItem().getName()));
    }
}
