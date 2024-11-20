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
    private Integer item;

    @NotNull
    private Integer booker;

    @NotNull
    @Future
    private LocalDate start;

    @NotNull
    @Future
    private LocalDate end;

}
