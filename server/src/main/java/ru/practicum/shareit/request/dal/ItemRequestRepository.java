package ru.practicum.shareit.request.dal;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.model.Request;

import java.util.List;
import java.util.Optional;

public interface ItemRequestRepository extends JpaRepository<Request, Long> {

    Optional<Request> findById(Long requestId);

    List<Request> findByRequestor(Long id);

    Request save(Request request);

    void deleteById(Long id);

}
