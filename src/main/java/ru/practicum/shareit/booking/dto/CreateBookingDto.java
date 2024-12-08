package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class CreateBookingDto {

    @NotNull
    @Future
    private LocalDate start;

    @NotNull
    @Future
    private LocalDate end;

    @NotNull
    private Long item;

    @NotNull
    private Long booker;

}
