package ru.practicum.shareit.request.dal;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.model.Request;

import java.util.Optional;

public interface ItemRequestRepository extends JpaRepository<Request, Long> {

    Optional<Request> findById(Long requestId);

    Request save(Request request);

    void deleteById(Long id);

}
