package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.validators.FutureDate;

import java.time.LocalDate;

@Data
@Builder
public class CreateItemRequestDto {

    @NotNull
    @FutureDate
    private LocalDate created;

    @NotBlank(message = "Request description must not be blank")
    @Size(max = 500)
    private String desciption;

}
