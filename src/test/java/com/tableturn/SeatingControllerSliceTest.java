package com.tableturn.seating;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.tableturn.error.TableTooSmall;

/** The refusal mapping alone: the web layer only, with the service mocked to throw. */
@WebMvcTest(SeatingController.class)
class SeatingControllerTest {

    @Autowired
    MockMvc http;

    @MockitoBean
    SeatingService seatings;

    @Test
    void aRefusalFromTheServiceIs409WithItsType() throws Exception {
        when(seatings.seat(anyString(), anyString(), anyString())).thenThrow(new TableTooSmall("T11", 4, 6));

        http.perform(post("/seatings").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"partyId\":\"W001\",\"table\":\"T11\"}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.type").value("/errors/table-too-small"));
    }
}