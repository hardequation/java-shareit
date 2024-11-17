package ru.practicum.shareit.request.model;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.validators.FutureDate;

import java.time.LocalDate;

@Data
@Builder
public class ItemRequest {

    @NotNull
    private Integer id;

    @NotNull
    private Integer requestor;

    @NotNull
    @FutureDate
    private LocalDate created;

    @NotBlank(message = "Request description must not be blank")
    @Size(max = 500)
    private String desciption;

}
