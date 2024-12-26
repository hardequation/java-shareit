package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.Constants;
import ru.practicum.shareit.item.dto.CreateCommentDto;
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;

import static ru.practicum.shareit.Constants.HEADER_USER_PARAMETER;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemClient itemClient;

    @GetMapping
    public ResponseEntity<Object> findAll(
            @RequestHeader(value = Constants.HEADER_USER_PARAMETER) Long userId) {
        return itemClient.findAll(userId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> findById(
            @RequestHeader(HEADER_USER_PARAMETER) long userId,
            @PathVariable Long itemId) {
        return itemClient.findById(userId, itemId);
    }

    @PostMapping
    public ResponseEntity<Object> createItem(
            @RequestHeader(Constants.HEADER_USER_PARAMETER) Long ownerId,
            @Valid @RequestBody CreateItemDto itemDto) {
        return itemClient.createItem(ownerId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(
            @RequestHeader(Constants.HEADER_USER_PARAMETER) Long userId,
            @PathVariable long itemId,
            @Valid @RequestBody UpdateItemDto dto) {
        return itemClient.updateItem(userId, itemId, dto);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(
            @RequestHeader(Constants.HEADER_USER_PARAMETER) Long userId,
            @RequestParam(name = "text") String text) {
        return itemClient.search(userId, text);
    }

    @DeleteMapping("/{itemId}")
    public void deleteById(@RequestHeader(Constants.HEADER_USER_PARAMETER) Long userId,
                           @PathVariable(name = "itemId") Long itemId) {
        itemClient.deleteById(userId, itemId);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> commentItem(
            @RequestHeader(Constants.HEADER_USER_PARAMETER) Long userId,
            @PathVariable(name = "itemId") Long itemId,
            @RequestBody CreateCommentDto commentDto) {
        return itemClient.commentItem(userId, itemId, commentDto);
    }

}
