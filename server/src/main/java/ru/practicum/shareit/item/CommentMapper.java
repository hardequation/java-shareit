package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;

@Component
public class CommentMapper {
    CommentDto map(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .item(comment.getItem().getId())
                .authorName(comment.getUser().getName())
                .text(comment.getText())
                .created(comment.getCreated())
                .build();
    }
}
