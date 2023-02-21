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
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.BookingServiceImpl;
import ru.practicum.shareit.booking.UserType;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
public class BookingControllerTest {
    @Mock
    private BookingServiceImpl bookingService;

    @InjectMocks
    private BookingController controller;

    private final ObjectMapper mapper = JsonMapper.builder()
            .findAndAddModules()
            .build();

    private MockMvc mvc;

    private Booking booking;

    private BookingDto bookingDto;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
        booking = new Booking();
        booking.setId(1);
        booking.setStatus(Booking.Status.WAITING);
        booking.setEnd(LocalDateTime.parse("1992-05-01T00:30:09"));
        booking.setStart(LocalDateTime.parse("1991-05-01T00:30:09"));
    }

    @Test
    public void getBooking() throws Exception {
        when(bookingService.getBooking(1, 1))
                .thenReturn(booking);

        mvc.perform(get("/bookings/{bookingId}", 1)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("WAITING"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.start[0]").value(1991))
                .andExpect(MockMvcResultMatchers.jsonPath("$.start[1]").value(05))
                .andExpect(MockMvcResultMatchers.jsonPath("$.start[2]").value(01))
                .andExpect(MockMvcResultMatchers.jsonPath("$.start[3]").value(00))
                .andExpect(MockMvcResultMatchers.jsonPath("$.start[4]").value(30))
                .andExpect(MockMvcResultMatchers.jsonPath("$.start[5]").value(9))
                .andExpect(MockMvcResultMatchers.jsonPath("$.end[0]").value(1992))
                .andExpect(MockMvcResultMatchers.jsonPath("$.end[1]").value(05))
                .andExpect(MockMvcResultMatchers.jsonPath("$.end[2]").value(01))
                .andExpect(MockMvcResultMatchers.jsonPath("$.end[3]").value(00))
                .andExpect(MockMvcResultMatchers.jsonPath("$.end[4]").value(30))
                .andExpect(MockMvcResultMatchers.jsonPath("$.end[5]").value(9));
    }


    @Test
    public void getBookingUser() throws Exception {
        List<Booking> bookingList = new ArrayList<>();
        bookingList.add(booking);

        when(bookingService.getAllBooking("ALL", 1, UserType.Type.BOOKER, 0, 10))
                .thenReturn(bookingList);

        mvc.perform(get("/bookings?state={state}", "ALL")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].status").value("WAITING"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].start[0]").value(1991))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].start[1]").value(05))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].start[2]").value(01))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].start[3]").value(00))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].start[4]").value(30))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].start[5]").value(9))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].end[0]").value(1992))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].end[1]").value(05))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].end[2]").value(01))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].end[3]").value(00))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].end[4]").value(30))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].end[5]").value(9));
    }

    @Test
    public void getBookingOwner() throws Exception {
        List<Booking> bookingList = new ArrayList<>();
        bookingList.add(booking);

        when(bookingService.getAllBooking("ALL", 1, UserType.Type.OWNER, 0, 10))
                .thenReturn(bookingList);

        mvc.perform(get("/bookings/owner?state={state}", "ALL")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].status").value("WAITING"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].start[0]").value(1991))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].start[1]").value(05))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].start[2]").value(01))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].start[3]").value(00))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].start[4]").value(30))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].start[5]").value(9))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].end[0]").value(1992))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].end[1]").value(05))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].end[2]").value(01))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].end[3]").value(00))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].end[4]").value(30))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].end[5]").value(9));
    }

    @Test
    public void createBooking() throws Exception {
        bookingDto = new BookingDto();
        bookingDto.setStart(LocalDateTime.parse("1991-05-01T00:30:09"));
        bookingDto.setEnd(LocalDateTime.parse("1992-05-01T00:30:09"));

        when(bookingService.creatBooking(bookingDto, 1)).thenReturn(booking);

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(booking))
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("WAITING"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.start[0]").value(1991))
                .andExpect(MockMvcResultMatchers.jsonPath("$.start[1]").value(05))
                .andExpect(MockMvcResultMatchers.jsonPath("$.start[2]").value(01))
                .andExpect(MockMvcResultMatchers.jsonPath("$.start[3]").value(00))
                .andExpect(MockMvcResultMatchers.jsonPath("$.start[4]").value(30))
                .andExpect(MockMvcResultMatchers.jsonPath("$.start[5]").value(9))
                .andExpect(MockMvcResultMatchers.jsonPath("$.end[0]").value(1992))
                .andExpect(MockMvcResultMatchers.jsonPath("$.end[1]").value(05))
                .andExpect(MockMvcResultMatchers.jsonPath("$.end[2]").value(01))
                .andExpect(MockMvcResultMatchers.jsonPath("$.end[3]").value(00))
                .andExpect(MockMvcResultMatchers.jsonPath("$.end[4]").value(30))
                .andExpect(MockMvcResultMatchers.jsonPath("$.end[5]").value(9));
    }

    @Test
    public void updateBooking() throws Exception {
        Booking newBooking = new Booking();
        newBooking.setStatus(Booking.Status.APPROVED);
        newBooking.setId(1);
        newBooking.setEnd(LocalDateTime.parse("1992-05-01T00:30:09"));
        newBooking.setStart(LocalDateTime.parse("1991-05-01T00:30:09"));

        when(bookingService.updateBookingApp(1, 1)).thenReturn(newBooking);

        mvc.perform(patch("/bookings/{bookingId}?approved={approved}", 1, "true")
                        .content(mapper.writeValueAsString(newBooking))
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("APPROVED"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.start[0]").value(1991))
                .andExpect(MockMvcResultMatchers.jsonPath("$.start[1]").value(05))
                .andExpect(MockMvcResultMatchers.jsonPath("$.start[2]").value(01))
                .andExpect(MockMvcResultMatchers.jsonPath("$.start[3]").value(00))
                .andExpect(MockMvcResultMatchers.jsonPath("$.start[4]").value(30))
                .andExpect(MockMvcResultMatchers.jsonPath("$.start[5]").value(9))
                .andExpect(MockMvcResultMatchers.jsonPath("$.end[0]").value(1992))
                .andExpect(MockMvcResultMatchers.jsonPath("$.end[1]").value(05))
                .andExpect(MockMvcResultMatchers.jsonPath("$.end[2]").value(01))
                .andExpect(MockMvcResultMatchers.jsonPath("$.end[3]").value(00))
                .andExpect(MockMvcResultMatchers.jsonPath("$.end[4]").value(30))
                .andExpect(MockMvcResultMatchers.jsonPath("$.end[5]").value(9));
    }

    @Test
    public void updateBooking1() throws Exception {
        Booking newBooking = new Booking();
        newBooking.setStatus(Booking.Status.REJECTED);
        newBooking.setId(1);
        newBooking.setEnd(LocalDateTime.parse("1992-05-01T00:30:09"));
        newBooking.setStart(LocalDateTime.parse("1991-05-01T00:30:09"));

        when(bookingService.updateBookingRej(1, 1)).thenReturn(newBooking);

        mvc.perform(patch("/bookings/{bookingId}?approved={approved}", 1, "false")
                        .content(mapper.writeValueAsString(newBooking))
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("REJECTED"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.start[0]").value(1991))
                .andExpect(MockMvcResultMatchers.jsonPath("$.start[1]").value(05))
                .andExpect(MockMvcResultMatchers.jsonPath("$.start[2]").value(01))
                .andExpect(MockMvcResultMatchers.jsonPath("$.start[3]").value(00))
                .andExpect(MockMvcResultMatchers.jsonPath("$.start[4]").value(30))
                .andExpect(MockMvcResultMatchers.jsonPath("$.start[5]").value(9))
                .andExpect(MockMvcResultMatchers.jsonPath("$.end[0]").value(1992))
                .andExpect(MockMvcResultMatchers.jsonPath("$.end[1]").value(05))
                .andExpect(MockMvcResultMatchers.jsonPath("$.end[2]").value(01))
                .andExpect(MockMvcResultMatchers.jsonPath("$.end[3]").value(00))
                .andExpect(MockMvcResultMatchers.jsonPath("$.end[4]").value(30))
                .andExpect(MockMvcResultMatchers.jsonPath("$.end[5]").value(9));
    }
}
