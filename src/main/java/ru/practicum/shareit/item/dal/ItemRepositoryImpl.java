package ru.practicum.shareit.item.dal;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class ItemRepositoryImpl implements ItemRepository {

    private final Map<Integer, List<Item>> userItemIndex = new LinkedHashMap<>();
    private Map<Integer, Item> itemStorage = new HashMap<>();

    @Override
    public Boolean contains(Integer id) {
        return itemStorage.containsKey(id);
    }

    @Override
    public Set<Item> findByUserId(Integer userId) {
        return new HashSet<>(userItemIndex.computeIfAbsent(userId, k -> new ArrayList<>()));
    }

    @Override
    public Set<Item> findAll() {
        return new HashSet<>(itemStorage.values());
    }

    @Override
    public Item find(Integer itemId) {
        return itemStorage.get(itemId);
    }

    @Override
    public Item add(Item item) {
        itemStorage.put(item.getId(), item);
        userItemIndex.computeIfAbsent(item.getOwner(), k -> new ArrayList<>()).add(item);
        return item;
    }

    @Override
    public Item update(Item item) {
        Item updatedItem = this.find(item.getId());

        if (item.getName() != null) {
            updatedItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            updatedItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            updatedItem.setAvailable(item.getAvailable());
        }
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
