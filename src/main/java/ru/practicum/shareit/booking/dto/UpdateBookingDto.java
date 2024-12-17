package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UpdateBookingDto {

    @Future
    private LocalDateTime start;

    @Future
    private LocalDateTime end;

    private Long item;

    private Long booker;

}
