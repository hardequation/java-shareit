package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ItemRequestDto {

    @NotBlank(message = "Request description must not be blank")
    @Size(max = 500)
    private String description;

    @NotNull
    private Long requestor;

    @NotNull
    @Future
    private LocalDateTime created;

}
