package ru.practicum.shareit.Dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.MappingComment;
import ru.practicum.shareit.item.model.Comments;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

public class MappingCommentTest {
    private MappingComment mappingComment;

    @BeforeEach
    void setUp() {
        mappingComment = new MappingComment();
    }

    @Test
    public void setMappingComment() {

        User user = new User();
        user.setName("User");

        Comments comments = new Comments();
        comments.setId(1);
        comments.setText("Comment");
        comments.setCreated(LocalDateTime.parse("1992-05-01T00:30:09"));
        comments.setAuthor(user);

        CommentDto commentDto = new CommentDto();
        commentDto.setId(1);
        commentDto.setText("Comment");
        commentDto.setCreated(LocalDateTime.parse("1992-05-01T00:30:09"));
        commentDto.setAuthorName("User");

        Assertions.assertEquals(mappingComment.mapCommentDto(comments), commentDto);
    }

}
