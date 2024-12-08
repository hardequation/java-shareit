package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dal.ItemRequestRepository;
import ru.practicum.shareit.request.model.ItemRequest;

import static ru.practicum.shareit.exception.ErrorMessages.REQUEST_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ItemRequestService {

    ItemRequestRepository repository;


    public ItemRequest findById(Long requestId) {
        return repository.findById(requestId).orElseThrow(() -> new NotFoundException(REQUEST_NOT_FOUND + requestId));
    }

    public ItemRequest save(ItemRequest request) {
        return repository.save(request);
    }

    public void deleteById(Long requestId) {
        repository.deleteById(requestId);
    }
}
