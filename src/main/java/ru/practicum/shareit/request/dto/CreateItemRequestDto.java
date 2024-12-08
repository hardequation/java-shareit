package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class CreateItemRequestDto {

    @NotNull
    @Size(max = 500)
    private String description;

    @NotNull
    private Long requestor;

    @NotNull
    @Future
    private LocalDate created;

}
