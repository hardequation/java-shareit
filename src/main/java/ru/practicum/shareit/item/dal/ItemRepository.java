package ru.practicum.shareit.item.dal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByOwner(User owner);

    List<Item> findAll();

    Optional<Item> findById(Long itemId);

    Item save(Item item);

    @Query("SELECT it " +
            "FROM Item as it " +
            "WHERE it.available = true " +
            "AND (UPPER(it.name) LIKE UPPER(?1) " +
            "OR UPPER(it.description) LIKE UPPER(?1))")
    List<Item> search(String value);

    void deleteById(Long id);

}