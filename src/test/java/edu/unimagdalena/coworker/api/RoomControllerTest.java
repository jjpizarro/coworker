package edu.unimagdalena.coworker.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.unimagdalena.coworker.api.dto.RoomDtos.*;
import edu.unimagdalena.coworker.exception.NotFoundException;
import edu.unimagdalena.coworker.services.RoomService;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RoomController.class)
class RoomControllerTest {

    @Autowired MockMvc mvc;
    @Autowired
    ObjectMapper om;
    @MockitoBean
    RoomService service;

    @Test
    void create_shouldReturn201AndLocation() throws Exception {
        var req = new RoomCreateRequest("Sala A", 8);
        var resp = new RoomResponse(7L,"Sala A",8,1L, java.util.Set.of());

        when(service.create(eq(1L), any())).thenReturn(resp);

        mvc.perform(post("/api/spaces/1/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", org.hamcrest.Matchers.endsWith("/api/rooms/7")))
                .andExpect(jsonPath("$.id").value(7));
    }

    @Test
    void available_shouldReturn200() throws Exception {
        var start = OffsetDateTime.now().plusDays(1).withHour(9);
        var end   = start.plusHours(2);

        when(service.findAvailable(eq(1L), any(), any(), eq(6)))
                .thenReturn(List.of(new RoomResponse(2L,"B",10,1L, java.util.Set.of())));

        mvc.perform(get("/api/spaces/1/rooms/available")
                        .param("start", start.toString())
                        .param("end", end.toString())
                        .param("minCapacity", "6"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(2));
    }

    @Test
    void get_shouldReturn404() throws Exception {
        when(service.get(99L)).thenThrow(new NotFoundException("Room 99 not found"));

        mvc.perform(get("/api/rooms/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Room 99 not found"));
    }

    @Test
    void delete_shouldReturn204() throws Exception {
        mvc.perform(delete("/api/rooms/8"))
                .andExpect(status().isNoContent());
        verify(service).delete(8L);
    }
}