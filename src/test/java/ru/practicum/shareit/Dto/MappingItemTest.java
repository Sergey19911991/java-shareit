package ru.practicum.shareit.Dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.practicum.shareit.item.CommenRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.MappingComment;
import ru.practicum.shareit.item.dto.MappingItem;
import ru.practicum.shareit.item.model.Comments;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

public class MappingItemTest {
    private MappingItem mappingItem;

    private CommenRepository commenRepository;

    private MappingComment mappingComment;

    @BeforeEach
    void setUp() {
        commenRepository = Mockito.mock(CommenRepository.class);
        mappingComment = Mockito.mock(MappingComment.class);
        mappingItem = new MappingItem(commenRepository, mappingComment);
    }

    @Test
    public void createItemDto() {
        List<Comments> list = new ArrayList<>();
        Comments comment = new Comments();
        CommentDto commentDto = new CommentDto();
        List<CommentDto> listComment = new ArrayList<>();

        Item item = new Item();
        item.setId(1);
        item.setDescription("Description");
        item.setAvailable(true);
        item.setName("Name");

        ItemDto itemDto = new ItemDto();
        itemDto.setId(1);
        itemDto.setDescription("Description");
        itemDto.setAvailable(true);
        itemDto.setName("Name");
        itemDto.setComments(listComment);

        when(commenRepository.getCommentsItem(1)).thenReturn(list);
        when(mappingComment.mapCommentDto(comment)).thenReturn(commentDto);

        Assertions.assertEquals(mappingItem.mapItemDto(item), itemDto);


    }
}
