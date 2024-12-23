package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CreateItemRequestDto {

    private String description;

    private Long requestor;

    private LocalDateTime created;

}
