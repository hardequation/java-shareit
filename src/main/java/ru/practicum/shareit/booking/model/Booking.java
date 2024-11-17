package ru.practicum.shareit.booking.model;


import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.validators.FutureDate;

import java.time.LocalDate;


@Data
@Builder
public class Booking {

    @NotNull
    private Integer id;

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

    @NotNull
    private BookingStatus status;

}
