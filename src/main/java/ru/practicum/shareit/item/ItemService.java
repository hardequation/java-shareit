package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.AuthentificationException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dal.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dal.UserRepository;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

import static ru.practicum.shareit.exception.ErrorMessages.ITEM_NOT_FOUND;
import static ru.practicum.shareit.exception.ErrorMessages.USER_NOT_FOUND;


@Service
@Slf4j
@RequiredArgsConstructor
public class ItemService {

    private static int idCounter = 0;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public Set<Item> getByUser(Integer userId) {
        if (Boolean.FALSE.equals(userRepository.contains(userId))) {
            throw new NotFoundException(USER_NOT_FOUND + userId);
        }
        return itemRepository.findByUserId(userId);
    }

    public Set<Item> findAll() {
        return itemRepository.findAll();
    }

    public Item get(Integer itemId) {
        if (Boolean.FALSE.equals(itemRepository.contains(itemId))) {
            throw new NotFoundException(ITEM_NOT_FOUND + itemId);
        }
        return itemRepository.find(itemId);
    }

    public Item add(Item item) {
        Integer ownerId = item.getOwner();
        if (Boolean.FALSE.equals(userRepository.contains(ownerId))) {
            throw new NotFoundException(USER_NOT_FOUND + ownerId);
        }
        item.setId(getCounter());
        return itemRepository.add(item);
    }

    public Item update(Integer requestor, Integer itemId, Item newItem) {
        if (Boolean.FALSE.equals(itemRepository.contains(itemId))) {
            throw new NotFoundException(ITEM_NOT_FOUND + itemId);
        }
        Item updatedItem = itemRepository.find(itemId);
        if (!Objects.equals(updatedItem.getOwner(), requestor)) {
            throw new AuthentificationException("Item can be updated only by owner");
        }
        return itemRepository.update(newItem);
    }

    public void remove(Integer itemId) {
        if (Boolean.FALSE.equals(itemRepository.contains(itemId))) {
            throw new NotFoundException(ITEM_NOT_FOUND + itemId);
        }
        itemRepository.remove(itemId);
    }

    public Set<Item> search(String text) {
        if (text.isBlank()) {
            return Collections.emptySet();
        }
        return itemRepository.search(text);
    }

    private synchronized int getCounter() {
        return ++idCounter;
    }
}
