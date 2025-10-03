package edu.unimagdalena.coworker.api;
import edu.unimagdalena.coworker.api.dto.ReservationDtos.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.unimagdalena.coworker.services.ReservationItemService;
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

@WebMvcTest(ReservationItemController.class)
class ReservationItemControllerTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper om;
    @MockitoBean
    ReservationItemService service;

    @Test
    void add_shouldReturn201AndLocation() throws Exception {
        var start = OffsetDateTime.now().plusHours(1);
        var end   = start.plusHours(2);
        var req = new ReservationItemCreateRequest(2L, start, end);
        var resp = new ReservationItemResponse(10L, 2L, "Sala A", start, end);

        when(service.addItem(eq(1L), any())).thenReturn(resp);

        mvc.perform(post("/api/reservations/1/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", org.hamcrest.Matchers.endsWith("/api/reservations/1/items/10")))
                .andExpect(jsonPath("$.id").value(10));
    }

    @Test
    void list_shouldReturn200() throws Exception {
        var resp = List.of(new ReservationItemResponse(1L, 2L, "Sala A", OffsetDateTime.now(), OffsetDateTime.now().plusHours(1)));
        when(service.listByReservation(1L)).thenReturn(resp);

        mvc.perform(get("/api/reservations/1/items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].roomName").value("Sala A"));
    }

    @Test
    void delete_shouldReturn204() throws Exception {
        mvc.perform(delete("/api/reservations/1/items/5"))
                .andExpect(status().isNoContent());
        verify(service).removeItem(5L);
    }
}