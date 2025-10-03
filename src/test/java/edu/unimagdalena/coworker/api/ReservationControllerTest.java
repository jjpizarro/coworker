package edu.unimagdalena.coworker.api;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.unimagdalena.coworker.api.dto.ReservationDtos.*;
import edu.unimagdalena.coworker.services.ReservationService;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReservationController.class)
class ReservationControllerTest {

    @Autowired MockMvc mvc;
    @Autowired
    ObjectMapper om;
    @MockitoBean
    ReservationService service;

    @Test
    void createDraft_shouldReturn201AndLocation() throws Exception {
        var req = new ReservationCreateRequest(1L);
        var resp = new ReservationResponse(9L,"Ana","ana@d.com", OffsetDateTime.now(), java.util.List.of());

        when(service.createDraft(any())).thenReturn(resp);

        mvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", org.hamcrest.Matchers.endsWith("/api/reservations/9")))
                .andExpect(jsonPath("$.id").value(9));
    }

    @Test
    void listByMember_shouldReturn200() throws Exception {
        var page = new PageImpl<>(java.util.List.of(
                new ReservationResponse(1L,"A","a@d.com", OffsetDateTime.now(), java.util.List.of())
        ));
        when(service.listByMember(eq("a@d.com"), any())).thenReturn(page);

        mvc.perform(get("/api/reservations?email=a@d.com&page=0&size=5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void cleanup_shouldReturn200AndInt() throws Exception {
        when(service.deleteOlderThan(any())).thenReturn(3);

        mvc.perform(delete("/api/reservations/cleanup")
                        .param("before", OffsetDateTime.now().minusDays(30).toString()))
                .andExpect(status().isOk())
                .andExpect(content().string("3"));
    }

    @Test
    void delete_shouldReturn204() throws Exception {
        mvc.perform(delete("/api/reservations/4"))
                .andExpect(status().isNoContent());
        verify(service).delete(4L);
    }
}