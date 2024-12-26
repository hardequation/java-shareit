package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {

    private static final String FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    private Long id;

    @JsonFormat(pattern = FORMAT)
    private LocalDateTime start;

    @JsonFormat(pattern = FORMAT)
    private LocalDateTime end;

    private Item item;

    private User booker;

    private BookingStatus status;

    public String getStartFormatted() {
        return start.format(DateTimeFormatter.ofPattern(FORMAT));
    }

    public String getEndFormatted() {
        return end.format(DateTimeFormatter.ofPattern(FORMAT));
    }

}
