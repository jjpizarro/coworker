package edu.unimagdalena.coworker.api;


import edu.unimagdalena.coworker.api.dto.MemberDtos.*;
import edu.unimagdalena.coworker.services.MemberService;
import edu.unimagdalena.coworker.exception.NotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
@WebMvcTest(MemberController.class)
class MemberControllerTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper om;
    @MockitoBean
    MemberService service;

    @Test
    void create_shouldReturn201AndLocation() throws Exception {
        var req = new MemberCreateRequest("Ana", "ana@d.com", new MemberProfileDto("+57","Uni"));
        var resp = new MemberResponse(10L, "Ana", "ana@d.com", new MemberProfileDto("+57","Uni"));

        when(service.create(any())).thenReturn(resp);

        mvc.perform(post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", org.hamcrest.Matchers.endsWith("/api/members/10")))
                .andExpect(jsonPath("$.id").value(10));
    }

    @Test
    void get_shouldReturn200() throws Exception {
        when(service.get(5L)).thenReturn(new MemberResponse(5L,"A","a@d.com",null));

        mvc.perform(get("/api/members/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5));
    }

    @Test
    void get_shouldReturn404WhenNotFound() throws Exception {
        when(service.get(99L)).thenThrow(new NotFoundException("Member 99 not found"));

        mvc.perform(get("/api/members/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Member 99 not found"));
    }

    @Test
    void delete_shouldReturn204() throws Exception {
        mvc.perform(delete("/api/members/3"))
                .andExpect(status().isNoContent());
        verify(service).delete(3L);
    }
}