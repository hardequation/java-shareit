package ru.practicum.shareit.request;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.CreateItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.Request;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ItemRequestMapper {

    public final Request map(Long userId, CreateItemRequestDto dto) {
        return Request.builder()
                .requestor(userId)
                .created(LocalDateTime.now())
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

    ItemRequestDto map(Request dto, List<ItemDto> items) {
        return ItemRequestDto.builder()
                .id(dto.getId())
                .requestor(dto.getRequestor())
                .created(dto.getCreated())
                .description(dto.getDescription())
                .items(items)
                .build();
    }
}
