package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCommentDto {

    @Size(max = 500)
    @NotBlank(message = "Comment text must not be blank")
    private String text;

//    private String authorName;

//    private LocalDateTime created;
}
