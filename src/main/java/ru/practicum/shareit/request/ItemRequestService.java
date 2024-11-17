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

    private static int idCounter = 0;
    ItemRequestRepository repository;

    public Boolean contains(Integer requestId) {
        if (Boolean.FALSE.equals(repository.contains(requestId))) {
            throw new NotFoundException(REQUEST_NOT_FOUND + requestId);
        }
        return repository.contains(requestId);
    }


    public ItemRequest findRequest(Integer requestId) {
        if (Boolean.FALSE.equals(repository.contains(requestId))) {
            throw new NotFoundException(REQUEST_NOT_FOUND + requestId);
        }
        return repository.findRequest(requestId);
    }

    public ItemRequest add(ItemRequest request) {
        request.setId(getCounter());
        return repository.add(request);
    }

    public ItemRequest update(ItemRequest request) {
        Integer requestId = request.getId();
        if (Boolean.FALSE.equals(repository.contains(requestId))) {
            throw new NotFoundException(REQUEST_NOT_FOUND + requestId);
        }
        return repository.update(request);
    }

    public void remove(Integer requestId) {
        if (Boolean.FALSE.equals(repository.contains(requestId))) {
            throw new NotFoundException(REQUEST_NOT_FOUND + requestId);
        }
        repository.remove(requestId);
    }

    private synchronized int getCounter() {
        return ++idCounter;
    }
}
