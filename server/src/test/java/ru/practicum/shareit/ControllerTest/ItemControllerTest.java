package ru.practicum.shareit.ControllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.ItemServiceImpl;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoInletOutlet;
import ru.practicum.shareit.item.model.Comments;
import ru.practicum.shareit.item.model.Item;

import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ItemControllerTest {
    @Mock
    private ItemServiceImpl itemServiceImpl;

    @InjectMocks
    private ItemController controller;

    private final ObjectMapper mapper = new ObjectMapper();
    private MockMvc mvc;

    private ItemDto item;

    private ItemDtoInletOutlet itemInlet;


    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
        item = new ItemDto();
        item.setId(1);
        item.setName("Item");
        item.setDescription("Item");
        item.setAvailable(true);
    }

    @Test
    public void createItem() throws Exception {
        itemInlet = new ItemDtoInletOutlet();
        itemInlet.setId(1);
        itemInlet.setName("Item");
        itemInlet.setDescription("Item");
        itemInlet.setAvailable(true);
        itemInlet.setRequestId(1);

        when(itemServiceImpl.creatItem(1, itemInlet))
                .thenReturn(itemInlet);

        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemInlet))
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Item"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Item"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.available").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.requestId").value(1));
    }

    @Test
    public void updateItem() throws Exception {
        Item item = new Item();
        item.setId(1);
        item.setName("Item");
        item.setDescription("Item");
        item.setAvailable(true);

        when(itemServiceImpl.updateItem(1, item, 1))
                .thenReturn(item);

        mvc.perform(patch("/items/{itemId}", 1)
                        .content(mapper.writeValueAsString(item))
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Item"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Item"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.available").value(true));

    }

    @Test
    public void getItem() throws Exception {
        when(itemServiceImpl.getItemById(anyInt(), anyInt()))
                .thenReturn(item);

        mvc.perform(get("/items/{itemId}", 1)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Item"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Item"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.available").value(true));
    }

    @Test
    public void getItemAll() throws Exception {
        Set<ItemDto> itemList = new TreeSet<>(Comparator.comparingInt(ItemDto::getId));
        itemList.add(item);

        when(itemServiceImpl.getItemAll(anyInt()))
                .thenReturn(itemList);

        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Item"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value("Item"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].available").value(true));
    }

    @Test
    public void getSearch() throws Exception {
        List<ItemDto> itemList = new ArrayList<>();
        itemList.add(item);

        when(itemServiceImpl.getSearch("em", 1))
                .thenReturn(itemList);

        mvc.perform(get("/items/search?text={text}", "em")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Item"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value("Item"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].available").value(true));
    }

    @Test
    public void createComment() throws Exception {
        Comments comments = new Comments();
        comments.setId(1);
        comments.setText("Comment");

        CommentDto commentDto = new CommentDto();
        commentDto.setId(1);
        commentDto.setText("Comment");


        when(itemServiceImpl.createComment(comments, 1, 1)).thenReturn(commentDto);


        mvc.perform(post("/items/{itemId}/comment", 1)
                        .content(mapper.writeValueAsString(commentDto))
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.text").value("Comment"));
    }


}
