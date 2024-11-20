package ru.practicum.shareit.item.dal;

import ru.practicum.shareit.item.model.Item;

import java.util.Set;

public interface ItemRepository {

    Boolean contains(Integer id);

    Set<Item> findByUserId(Integer userId);

    Set<Item> findAll();

    Item find(Integer itemId);

    Item add(Item item);

    Item update(Item item);

    Set<Item> search(String text);

    void remove(Integer id);

}