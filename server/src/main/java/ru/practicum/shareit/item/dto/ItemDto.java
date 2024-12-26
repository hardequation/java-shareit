package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {

    private static final String FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    private Long id;

    private String name;

    private String description;

    private Boolean available;

    private Long owner;

    private Long request;

    @JsonFormat(pattern = FORMAT)
    private LocalDateTime lastBooking;

    @JsonFormat(pattern = FORMAT)
    private LocalDateTime nextBooking;

    private List<String> comments;

    public String formatLastBooking() {
        return lastBooking.format(DateTimeFormatter.ofPattern(FORMAT));
    }

    public String formatNextBooking() {
        return nextBooking.format(DateTimeFormatter.ofPattern(FORMAT));
    }

}
