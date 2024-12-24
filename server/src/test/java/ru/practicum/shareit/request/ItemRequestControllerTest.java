package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.request.dto.CreateItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ItemRequestControllerTest {

    @Mock
    private ItemRequestService service;

    @InjectMocks
    private ItemRequestController controller;

    private MockMvc mvc;
    private ObjectMapper objectMapper;

    private ItemRequestDto requestDto;
    private CreateItemRequestDto createDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
        mvc = MockMvcBuilders.standaloneSetup(controller).build();

        // Тестовые данные
        requestDto = ItemRequestDto.builder()
                .id(1L)
                .description("Test description")
                .requestor(1L)
                .created(LocalDateTime.of(2024, 12, 23, 17, 37, 55))
                .build();

        createDto = CreateItemRequestDto.builder()
                .description("Test description")
                .build();
    }

    @Test
    void createItemRequest() throws Exception {
        when(service.save(eq(1L), any(CreateItemRequestDto.class))).thenReturn(requestDto);

        mvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .content(objectMapper.writeValueAsString(createDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(requestDto.getId()))
                .andExpect(jsonPath("$.description").value(requestDto.getDescription()))
                .andExpect(jsonPath("$.requestor").value(requestDto.getRequestor()))
                .andExpect(jsonPath("$.created").value(requestDto.getCreated().toString()));
    }

    @Test
    void findByUser() throws Exception {
        List<ItemRequestDto> requests = Arrays.asList(requestDto);
        when(service.findByRequester(1L)).thenReturn(requests);

        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id").value(requestDto.getId()))
                .andExpect(jsonPath("$[0].description").value(requestDto.getDescription()))
                .andExpect(jsonPath("$[0].requestor").value(requestDto.getRequestor()))
                .andExpect(jsonPath("$[0].created").value(requestDto.getCreated().toString()));
    }

    @Test
    void findAllRequests() throws Exception {
        List<ItemRequestDto> requests = Arrays.asList(requestDto);
        when(service.findAll(1L)).thenReturn(requests);

        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id").value(requestDto.getId()))
                .andExpect(jsonPath("$[0].description").value(requestDto.getDescription()))
                .andExpect(jsonPath("$[0].requestor").value(requestDto.getRequestor()))
                .andExpect(jsonPath("$[0].created").value(requestDto.getCreated().toString()));
    }

    @Test
    void findByRequestId() throws Exception {
        when(service.findById(2L, 1L)).thenReturn(requestDto);

        mvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", 2L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(requestDto.getId()))
                .andExpect(jsonPath("$.description").value(requestDto.getDescription()))
                .andExpect(jsonPath("$.requestor").value(requestDto.getRequestor()))
                .andExpect(jsonPath("$.created").value(requestDto.getCreated().toString()));
    }
}
