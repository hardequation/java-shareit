package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateItemDto {

    private String name;

    private String description;

    private Boolean available;

    private Long requestId; // if item is created in reply to someone's Request

}
