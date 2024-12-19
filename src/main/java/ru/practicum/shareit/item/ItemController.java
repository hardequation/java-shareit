package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CreateCommentDto;
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;

import java.util.List;

import static ru.practicum.shareit.Constants.HEADER_USER_PARAMETER;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping
    public List<ItemDto> findAll(@RequestHeader(value = HEADER_USER_PARAMETER) Long userId) {
        return itemService.findAll(userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto findById(@PathVariable Long itemId) {
        return itemService.findById(itemId);
    }

    @PostMapping
    public ItemDto save(@RequestHeader(HEADER_USER_PARAMETER) Long ownerId, @Valid @RequestBody CreateItemDto itemDto) {
        return itemService.save(ownerId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader(HEADER_USER_PARAMETER) Long userId,
                          @PathVariable long itemId,
                          @Valid @RequestBody UpdateItemDto dto) {
        return itemService.save(userId, itemId, dto);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestHeader(HEADER_USER_PARAMETER) Long userId,
                                @RequestParam() String text) {
        return itemService.search(text);
    }

    @DeleteMapping("/{itemId}")
    public void deleteById(@PathVariable(name = "itemId") Long itemId) {
        itemService.deleteById(itemId);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto commentItem(@RequestHeader(HEADER_USER_PARAMETER) Long userId,
                                  @PathVariable(name = "itemId") Long itemId,
                                  @RequestBody CreateCommentDto commentDto) {
        return itemService.commentItem(userId, itemId, commentDto);
    }

}
