package ru.practicum.shareit.item.dal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByOwner(Long ownerId);

    List<Item> findAll();

    Optional<Item> findById(Long itemId);

    Item save(Item item);

    @Query("SELECT it " +
            "FROM Item as it " +
            "WHERE it.available = true " +
            "AND (LOWER(it.name) LIKE ?1 " +
            "OR LOWER(it.description) LIKE ?1)")
    List<Item> search(String value);

    void deleteById(Long id);

}