package ru.practicum.shareit.request.dal;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.Optional;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    Optional<ItemRequest> findById(Long requestId);

    ItemRequest save(ItemRequest request);

    void deleteById(Long id);

}
