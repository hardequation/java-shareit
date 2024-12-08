package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
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
        Item item = itemService.findById(itemId);
        return itemMapper.map(item);
    }

    @PostMapping
    public ItemDto save(@RequestHeader(HEADER_USER_PARAMETER) Long userId, @Valid @RequestBody CreateItemDto itemDto) {
        Item item = itemMapper.map(itemDto, userId);
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

}
