package ru.practicum.shareit.request.dal;

import ru.practicum.shareit.request.model.ItemRequest;

public interface ItemRequestRepository {

    Boolean contains(Integer id);

    ItemRequest find(Integer requestId);

    ItemRequest add(ItemRequest request);

    ItemRequest update(ItemRequest request);

    void remove(Integer id);

}
