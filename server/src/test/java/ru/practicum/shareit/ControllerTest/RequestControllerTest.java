package ru.practicum.shareit.ControllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
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
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestController;
import ru.practicum.shareit.request.RequestServiceImpl;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoOutlet;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class RequestControllerTest {
    @Mock
    private RequestServiceImpl requestService;

    @InjectMocks
    private ItemRequestController controller;

    private final ObjectMapper mapper = JsonMapper.builder()
            .findAndAddModules()
            .build();

    private MockMvc mvc;

    private ItemRequestDtoOutlet itemRequestDtoOutlet;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
        itemRequestDtoOutlet = new ItemRequestDtoOutlet();
        itemRequestDtoOutlet.setId(1);
        itemRequestDtoOutlet.setDescription("Request");
        itemRequestDtoOutlet.setCreated(LocalDateTime.parse("1992-05-01T00:30:09"));
    }

    @Test
    public void getRequestById() throws Exception {
        when(requestService.getRequestById(1, 1))
                .thenReturn(itemRequestDtoOutlet);

        mvc.perform(get("/requests/{requestId}", 1)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Request"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.created[0]").value(1992))
                .andExpect(MockMvcResultMatchers.jsonPath("$.created[1]").value(05))
                .andExpect(MockMvcResultMatchers.jsonPath("$.created[2]").value(01))
                .andExpect(MockMvcResultMatchers.jsonPath("$.created[3]").value(00))
                .andExpect(MockMvcResultMatchers.jsonPath("$.created[4]").value(30))
                .andExpect(MockMvcResultMatchers.jsonPath("$.created[5]").value(9));
    }

    @Test
    public void getRequests() throws Exception {
        List<ItemRequestDtoOutlet> list = new ArrayList<>();
        list.add(itemRequestDtoOutlet);

        when(requestService.getRequests(1))
                .thenReturn(list);

        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value("Request"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].created[0]").value(1992))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].created[1]").value(05))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].created[2]").value(01))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].created[3]").value(00))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].created[4]").value(30))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].created[5]").value(9));
    }

    @Test
    public void getAll() throws Exception {
        List<ItemRequestDtoOutlet> list = new ArrayList<>();
        list.add(itemRequestDtoOutlet);

        when(requestService.getRequestsAll(1, null, null))
                .thenReturn(list);

        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value("Request"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].created[0]").value(1992))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].created[1]").value(05))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].created[2]").value(01))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].created[3]").value(00))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].created[4]").value(30))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].created[5]").value(9));
    }

    @Test
    public void createRequest() throws Exception {
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("Request");
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1);
        itemRequest.setDescription("Request");

        when(requestService.createRequest(itemRequestDto, 1)).thenReturn(itemRequest);

        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(itemRequest))
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Request"));
    }


}
