package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dal.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dal.UserRepository;

import java.util.Collections;
import java.util.List;

import static ru.practicum.shareit.exception.ErrorMessages.ITEM_NOT_FOUND;
import static ru.practicum.shareit.exception.ErrorMessages.USER_NOT_FOUND;


@Service
@Slf4j
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public List<Item> findByOwner(Long owner) {
        if (userRepository.findById(owner).isEmpty()) {
            throw new NotFoundException(USER_NOT_FOUND + owner);
        }
        return itemRepository.findByOwner(owner);
    }

    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    public Item findById(Long itemId) {
        return itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException(ITEM_NOT_FOUND + itemId));
    }

    public Item save(Item item) {
        Long ownerId = item.getOwner();
        if (userRepository.findById(ownerId).isEmpty()) {
            throw new NotFoundException(USER_NOT_FOUND + ownerId);
        }
        return itemRepository.save(item);
    }

    public void deleteById(Long itemId) {
        if (itemRepository.findById(itemId).isEmpty()) {
            throw new NotFoundException(ITEM_NOT_FOUND + itemId);
        }
        itemRepository.deleteById(itemId);
    }

    public List<Item> search(String text) {
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        return itemRepository.search(text);
    }
}
