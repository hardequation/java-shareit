package ru.practicum.shareit.request.dal;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.HashMap;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class ItemRequestRepositoryImpl implements ItemRequestRepository {

    Map<Integer, ItemRequest> repository = new HashMap<>();

    @Override
    public Boolean contains(Integer id) {
        return repository.containsKey(id);
    }

    @Override
    public ItemRequest findRequest(Integer requestId) {
        return repository.get(requestId);
    }

    @Override
    public ItemRequest add(ItemRequest request) {
        repository.put(request.getId(), request);
        return request;
    }

    @Override
    public ItemRequest update(ItemRequest request) {
        repository.put(request.getId(), request);
        return request;
    }

    @Override
    public void remove(Integer id) {
        repository.remove(id);
    }
}
