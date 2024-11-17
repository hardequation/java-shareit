package ru.practicum.shareit.request;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.request.dto.CreateItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

@Component
public class ItemRequestMapper {

    ItemRequest map(CreateItemRequestDto dto, Integer requestor) {
        return ItemRequest.builder()
                .requestor(requestor)
                .created(dto.getCreated())
                .desciption(dto.getDesciption())
                .build();
    }

    ItemRequest map(ItemRequestDto dto) {
        return ItemRequest.builder()
                .id(dto.getId())
                .requestor(dto.getRequestor())
                .created(dto.getCreated())
                .desciption(dto.getDesciption())
                .build();
    }

    ItemRequestDto map(ItemRequest dto) {
        return ItemRequestDto.builder()
                .id(dto.getId())
                .requestor(dto.getRequestor())
                .created(dto.getCreated())
                .desciption(dto.getDesciption())
                .build();
    }
}
