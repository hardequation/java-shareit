package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateItemDto {

    @NotBlank(message = "Name of item must not be blank")
    @Size(max = 100)
    private String name;

    @NotBlank(message = "Description of item must not be blank")
    @Size(max = 500)
    private String description;

    @NotNull
    private Boolean available;

    private Long requestId;

}
