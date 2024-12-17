package ru.practicum.shareit.request;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.request.dto.CreateItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.Request;

@Component
public class ItemRequestMapper {

    public final Request map(CreateItemRequestDto dto) {
        return Request.builder()
                .requestor(dto.getRequestor())
                .created(dto.getCreated())
                .description(dto.getDescription())
                .build();
    }

    public final Request map(ItemRequestDto dto) {
        return Request.builder()
                .id(dto.getId())
                .requestor(dto.getRequestor())
                .created(dto.getCreated())
                .description(dto.getDescription())
                .build();
    }

    ItemRequestDto map(Request dto) {
        return ItemRequestDto.builder()
                .id(dto.getId())
                .requestor(dto.getRequestor())
                .created(dto.getCreated())
                .description(dto.getDescription())
                .build();
    }
}
