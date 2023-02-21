package ru.practicum.shareit.item.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Comments;

@RequiredArgsConstructor
@Service
public class MappingComment {
    //из comments в dto
    public CommentDto mapCommentDto(Comments comment) {
        CommentDto dto = new CommentDto();
        dto.setId(comment.getId());
        dto.setCreated(comment.getCreated());
        dto.setText(comment.getText());
        dto.setAuthorName(comment.getAuthor().getName());
        return dto;
    }
}
