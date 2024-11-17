package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.validators.FutureDate;

import java.time.LocalDate;

@Data
@Builder
public class CreateBookingDto {

    @NotNull
    private Integer item;

    @NotNull
    private Integer booker;

    @NotNull
    @FutureDate
    private LocalDate start;

    @NotNull
    @FutureDate
    private LocalDate end;

}
