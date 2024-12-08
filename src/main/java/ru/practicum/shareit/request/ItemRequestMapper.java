package ru.practicum.shareit.request;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.request.dto.CreateItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

@Component
public class ItemRequestMapper {

    public final ItemRequest map(CreateItemRequestDto dto) {
        return ItemRequest.builder()
                .requestor(dto.getRequestor())
                .created(dto.getCreated())
                .description(dto.getDescription())
                .build();
    }

    public final ItemRequest map(ItemRequestDto dto) {
        return ItemRequest.builder()
                .id(dto.getId())
                .requestor(dto.getRequestor())
                .created(dto.getCreated())
                .description(dto.getDescription())
                .build();
    }

    ItemRequestDto map(ItemRequest dto) {
        return ItemRequestDto.builder()
                .id(dto.getId())
                .requestor(dto.getRequestor())
                .created(dto.getCreated())
                .description(dto.getDescription())
                .build();
    }
}
