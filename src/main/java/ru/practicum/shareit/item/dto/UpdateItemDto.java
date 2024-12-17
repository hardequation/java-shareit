package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateItemDto {

    @Size(max = 100)
    private String name;

    @Size(max = 500)
    private String description;

    private Boolean available;

}
