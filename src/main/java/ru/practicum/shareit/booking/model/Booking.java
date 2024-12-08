package ru.practicum.shareit.booking.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;


@Data
@Entity
@Table(name = "bookings", schema = "public")
@Builder
public class Booking {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @NotNull
    private BookingStatus status;

}
