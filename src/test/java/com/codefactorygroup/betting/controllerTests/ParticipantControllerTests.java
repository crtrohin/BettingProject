package com.codefactorygroup.betting.controllerTests;

import com.codefactorygroup.betting.controller.ParticipantController;
import com.codefactorygroup.betting.dto.ParticipantDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class ParticipantControllerTests {
    @Autowired
    private ParticipantController participantController;
    @Autowired
    private MockMvc mockMvc;

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getParticipantShouldReturnParticipant() throws Exception {
        this.mockMvc
                .perform(get("/participants/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name").value("Arsenal"));
    }

    @Test
    void getParticipantShouldReturnException() throws Exception {
        this.mockMvc
                .perform(get("/participants/100")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andExpect(result -> assertEquals("Participant with ID = 100 doesn't exist.", result.getResolvedException().getMessage()));
    }

    @Test
    void addParticipantShouldReturnParticipant() throws Exception {
        ParticipantDTO participantDTO = new ParticipantDTO(100, "New Arsenal");

        this.mockMvc
                .perform(post("/participants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(participantDTO))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Arsenal"));
    }

    @Test
    void addParticipantShoudlReturnException() throws Exception {
        ParticipantDTO participantDTO = new ParticipantDTO(1, "Real Madrid");

        this.mockMvc
                .perform(post("/participants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(participantDTO))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(result -> assertEquals("Participant with ID = 1 already exists.", result.getResolvedException().getMessage()));
    }

    @Test
    void updateParticipantShouldReturnParticipant() throws Exception {
        ParticipantDTO participantDTO = new ParticipantDTO(1, "Juventus");
        this.mockMvc
                .perform(put("/participants/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(participantDTO))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name").value("Juventus"));
    }

    @Test
    void updateParticipantShouldReturnException() throws Exception {
        ParticipantDTO participantDTO = new ParticipantDTO(100, "Juventus");
        this.mockMvc
                .perform(put("/participants/100")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(participantDTO))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andExpect(result -> assertEquals("Participant with ID = 100 doesn't exist.", result.getResolvedException().getMessage()));
    }
}