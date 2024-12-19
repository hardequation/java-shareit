package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class CommentDto {

    private Long id;

    private Long item;

    private String authorName;

    private String text;

    private LocalDate created;
}
