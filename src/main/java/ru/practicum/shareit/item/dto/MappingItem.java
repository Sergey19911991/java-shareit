package ru.practicum.shareit.item.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.CommenRepository;
import ru.practicum.shareit.item.model.Comments;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MappingItem {
    private final CommenRepository commenRepository;

    private final MappingComment mappingComment;

    //из item в dto
    public ItemDto mapItemDto(Item item) {
        ItemDto dto = new ItemDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setAvailable(item.getAvailable());
        List<CommentDto> list = new ArrayList<>();
        for (Comments comment : commenRepository.getCommentsItem(item.getId())) {
            list.add(mappingComment.mapCommentDto(comment));
        }
        dto.setComments(list);
        return dto;
    }

}
