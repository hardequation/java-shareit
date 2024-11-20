package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
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
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private static final String HEADER_USER_PARAMETER = "X-Sharer-User-Id";

    private final ItemService itemService;

    private final ItemMapper itemMapper;

    @GetMapping
    public Set<ItemDto> getAll(@RequestHeader(value = HEADER_USER_PARAMETER) Integer userId) {
        Set<Item> items;
        if (userId != null) {
            items = itemService.getByUser(userId);
        } else {
            items = itemService.findAll();
        }
        return items.stream()
                .map(itemMapper::map)
                .collect(Collectors.toSet());
    }

    @GetMapping("/{itemId}")
    public ItemDto get(@PathVariable Integer itemId) {
        Item item = itemService.get(itemId);
        return itemMapper.map(item);
    }

    @PostMapping
    public ItemDto add(@RequestHeader(HEADER_USER_PARAMETER) Integer userId, @Valid @RequestBody CreateItemDto itemDto) {
        Item item = itemMapper.map(itemDto, userId);
        Item addedItem = itemService.add(item);
        return itemMapper.map(addedItem);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader(HEADER_USER_PARAMETER) Integer userId,
                          @PathVariable Integer itemId,
                          @Valid @RequestBody UpdateItemDto newItemDto) {
        if (newItemDto == null) {
            throw new ValidationException("No fields in item, that are going to be updated");
        }
        Item item = itemMapper.map(newItemDto, itemId);
        Item updatedItem = itemService.update(userId, itemId, item);
        return itemMapper.map(updatedItem);
    }

    @GetMapping("/search")
    public Set<ItemDto> search(@RequestHeader(HEADER_USER_PARAMETER) Integer userId,
                               @RequestParam() String text) {
        return itemService.search(text).stream()
                .map(itemMapper::map)
                .collect(Collectors.toSet());
    }

    @DeleteMapping("/{itemId}")
    public void remove(@PathVariable(name = "itemId") Integer itemId) {
        itemService.remove(itemId);
    }

}
