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
                .andExpect(result -> assertEquals("Participant with ID=100 doesn't exist.",
                        result.getResolvedException().getMessage()));
    }

    @Test
    void getAllParticipantsShouldReturnParticipants() throws Exception {
        this.mockMvc.perform(get("/participants")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.[4].name").value("Liverpool"));
    }

    @Test
    void getParticipantsByEventIdShouldReturnParticipants() throws Exception {
        this.mockMvc.perform(get("/events/1/participants")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.[0].name").value("Senegal"));
    }

    @Test
    void getParticipantsByEventIdShouldReturnException() throws Exception {
        this.mockMvc.perform(get("/events/100/participants")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andExpect(result -> assertEquals("Event with ID=100 doesn't exist.",
                        result.getResolvedException().getMessage()));
    }


    @Test
    void addParticipantShouldReturnParticipant() throws Exception {
        ParticipantDTO participantDTO = ParticipantDTO.builder()
                .name("FC Sheriff")
                .build();

        this.mockMvc
                .perform(post("/events/1/participants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(participantDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("FC Sheriff"));
    }

    @Test
    void addParticipantShouldReturnExceptionAlreadyExists() throws Exception {
        ParticipantDTO participantDTO = ParticipantDTO.builder()
                .name("Arsenal")
                .build();

        this.mockMvc
                .perform(post("/events/1/participants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(participantDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(result -> assertEquals(String.format("Participant with name=%s already exists.", participantDTO.name()),
                        result.getResolvedException().getMessage()));

    }

    @Test
    void addParticipantShouldReturnNoEventExists() throws Exception {
        ParticipantDTO participantDTO = ParticipantDTO.builder()
                .name("Arsenal")
                .build();

        this.mockMvc
                .perform(post("/events/100/participants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(participantDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals(String.format("Event with ID=100 doesn't exist."),
                        result.getResolvedException().getMessage()));
    }

    @Test
    void updateParticipantShouldReturnParticipant() throws Exception {
        ParticipantDTO participantDTO = ParticipantDTO
                .builder()
                .name("Juventus")
                .build();

        this.mockMvc
                .perform(put("/participants/50")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(participantDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Juventus"));
    }

    @Test
    void updateParticipantShouldReturnException() throws Exception {
        ParticipantDTO participantDTO = ParticipantDTO.builder()
                .name("Juventus")
                .build();

        this.mockMvc
                .perform(put("/participants/100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(participantDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andExpect(result -> assertEquals("Participant with ID=100 doesn't exist.",
                        result.getResolvedException().getMessage()));
    }

    @Test
    void deleteParticipant() throws Exception {
        this.mockMvc
                .perform(delete("/participants/10"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void deleteParticipantShouldReturnException() throws Exception {
        this.mockMvc
                .perform(delete("/participants/100"))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
}