package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import static ru.practicum.shareit.Constants.HEADER_USER_PARAMETER;

/**
 * TODO Sprint add-requests.
 */

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private final RequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> createRequest(
            @RequestHeader(HEADER_USER_PARAMETER) Long userId,
            @RequestBody ItemRequestDto dto
    ) {
        return requestClient.createRequest(userId, dto);
    }

    @GetMapping
    public ResponseEntity<Object> findByUser(
            @RequestHeader(HEADER_USER_PARAMETER) Long userId) {
        return requestClient.findByUser(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> findAll(
            @RequestHeader(HEADER_USER_PARAMETER) Long userId) {
        return requestClient.findAll(userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> findByRequest(
            @RequestHeader(HEADER_USER_PARAMETER) Long userId,
            @PathVariable Long requestId
    ) {
        return requestClient.findByRequest(userId, requestId);
    }
}
