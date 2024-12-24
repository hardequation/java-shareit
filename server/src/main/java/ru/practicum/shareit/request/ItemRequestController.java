package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.request.dto.CreateItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.UserService;

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

    private final UserService userService;

    private final ItemService itemService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemRequestDto create(@RequestHeader(HEADER_USER_PARAMETER) Long userId,
                                 @RequestBody CreateItemRequestDto dto) {
        return service.save(userId, dto);
    }

    @GetMapping
    public List<ItemRequestDto> findByUser(@RequestHeader(HEADER_USER_PARAMETER) Long userId) {
        return service.findByRequester(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> findAll(@RequestHeader(HEADER_USER_PARAMETER) Long userId) {
        return service.findAll(userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto findByRequest(
            @RequestHeader(HEADER_USER_PARAMETER) Long userId,
            @PathVariable Long requestId) {
        return service.findById(userId, requestId);
    }
}
