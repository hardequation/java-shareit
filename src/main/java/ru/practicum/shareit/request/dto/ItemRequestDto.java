package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class ItemRequestDto {

    @NotNull
    private Integer id;

    @NotNull
    private Integer requestor;

    @NotNull
    @Future
    private LocalDate created;

    @NotBlank(message = "Request description must not be blank")
    @Size(max = 500)
    private String desciption;

}
