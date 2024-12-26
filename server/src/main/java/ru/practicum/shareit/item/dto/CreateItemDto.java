package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateItemDto {

    private String name;

    private String description;

    private Boolean available;

    private Long requestId; // if item is created in reply to someone's Request

}
