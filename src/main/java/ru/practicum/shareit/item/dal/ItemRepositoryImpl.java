package ru.practicum.shareit.item.dal;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class ItemRepositoryImpl implements ItemRepository {

    Map<Integer, Item> itemStorage = new HashMap<>();

    @Override
    public Boolean contains(Integer id) {
        return itemStorage.containsKey(id);
    }

    @Override
    public Set<Item> findItemsByUserId(Integer userId) {
        return itemStorage.values().stream()
                .filter(item -> Objects.equals(item.getOwner(), userId))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Item> findAll() {
        return new HashSet<>(itemStorage.values());
    }

    @Override
    public Item findItem(Integer itemId) {
        return itemStorage.get(itemId);
    }

    @Override
    public Item add(Item item) {
        itemStorage.put(item.getId(), item);
        return item;
    }

    @Override
    public Item update(Item item) {
        Item updatedItem = this.findItem(item.getId());
        if (item.getName() != null) {
            updatedItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            updatedItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            updatedItem.setAvailable(item.getAvailable());
        }
        itemStorage.put(item.getId(), item);
        return item;
    }

    @Override
    public Set<Item> search(String text) {
        return itemStorage.values().stream()
                .filter(item -> Objects.equals(item.getAvailable(), true) &&
                        item.getName() != null && item.getDescription() != null && (
                            item.getName().toLowerCase().contains(text.toLowerCase()) ||
                            item.getDescription().toLowerCase().contains(text.toLowerCase())
                            )
                )
                .collect(Collectors.toSet());
    }

    @Override
    public void remove(Integer id) {
        itemStorage.remove(id);
    }
}
