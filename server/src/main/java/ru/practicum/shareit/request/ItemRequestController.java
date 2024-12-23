package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

import static ru.practicum.shareit.Constants.HEADER_USER_PARAMETER;

/**
 * TODO Sprint add-requests.
 */

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestService service;

    @PostMapping
    public ItemRequestDto create(@RequestHeader(HEADER_USER_PARAMETER) Long userId,
                                 ItemRequestDto dto) {
        return service.save(dto);
    }

    @GetMapping
    public List<ItemRequestDto> findByUser(@RequestHeader(HEADER_USER_PARAMETER) Long userId) {
        return null;
    }

    @GetMapping("/all")
    public List<ItemRequestDto> findAll(@RequestHeader(HEADER_USER_PARAMETER) Long userId) {
        return null;
    }

    @GetMapping("/{requestId}")
    public List<ItemRequestDto> findByRequest(@PathVariable Long requestId) {
        return null;
    }
}
