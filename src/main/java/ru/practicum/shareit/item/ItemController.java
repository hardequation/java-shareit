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
import ru.practicum.shareit.exception.AuthentificationException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CreateCommentDto;
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.practicum.shareit.Constants.HEADER_USER_PARAMETER;
import static ru.practicum.shareit.exception.ErrorMessages.ITEM_NOT_FOUND;
import static ru.practicum.shareit.exception.ErrorMessages.USER_NOT_FOUND;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    private final UserService userService;

    private final ItemMapper itemMapper;

    private final CommentMapper commentMapper;

    @GetMapping
    public List<ItemDto> findAll(@RequestHeader(value = HEADER_USER_PARAMETER) Long userId) {
        List<Item> items;
        if (userId != null) {
            items = itemService.findByOwner(userId);
        } else {
            items = itemService.findAll();
        }
        return items.stream()
                .map(itemMapper::map)
                .toList();
    }

    @GetMapping("/{itemId}")
    public ItemDto findById(@PathVariable Long itemId) {
        Item item = itemService.findById(itemId).orElseThrow(() -> new NotFoundException(ITEM_NOT_FOUND + itemId));
        return itemMapper.map(item);
    }

    @PostMapping
    public ItemDto save(@RequestHeader(HEADER_USER_PARAMETER) Long ownerId, @Valid @RequestBody CreateItemDto itemDto) {
        User owner = userService.findById(ownerId).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND + ownerId));
        Item item = itemMapper.map(itemDto, owner);
        Item addedItem = itemService.save(item);
        return itemMapper.map(addedItem);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader(HEADER_USER_PARAMETER) Long userId,
                          @PathVariable long itemId,
                          @Valid @RequestBody UpdateItemDto dto) {
        Item oldItem = itemService.findById(itemId).orElseThrow(() -> new NotFoundException(ITEM_NOT_FOUND + itemId));
        if (!oldItem.getOwner().getId().equals(userId)) {
            throw new AuthentificationException("Only owner can update item");
        }
        Item item = itemMapper.map(dto, itemId, oldItem);

        item.setOwner(oldItem.getOwner());
        Item addedItem = itemService.save(item);
        return itemMapper.map(addedItem);
    }

    @GetMapping("/search")
    public Set<ItemDto> search(@RequestHeader(HEADER_USER_PARAMETER) Long userId,
                               @RequestParam() String text) {
        return itemService.search(text).stream()
                .map(itemMapper::map)
                .collect(Collectors.toSet());
    }

    @DeleteMapping("/{itemId}")
    public void deleteById(@PathVariable(name = "itemId") Long itemId) {
        itemService.deleteById(itemId);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto commentItem(@RequestHeader(HEADER_USER_PARAMETER) Long userId,
                                  @PathVariable(name = "itemId") Long itemId,
                                  @RequestBody CreateCommentDto commentDto) {

        Item item = itemService.findById(itemId).orElseThrow(() -> new NotFoundException(ITEM_NOT_FOUND + itemId));
        User user = userService.findById(userId).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND + userId));
        Comment comment = Comment.builder()
                .item(item)
                .user(user)
                .text(commentDto.getText())
                .created(LocalDate.now())
                .build();

        itemService.commentItem(comment);
        return commentMapper.map(comment);
    }

}
