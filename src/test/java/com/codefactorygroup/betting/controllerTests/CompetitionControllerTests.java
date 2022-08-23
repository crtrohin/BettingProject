package com.codefactorygroup.betting.controllerTests;

import com.codefactorygroup.betting.controller.CompetitionController;
import com.codefactorygroup.betting.dto.CompetitionDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class CompetitionControllerTests {
    @Autowired
    private CompetitionController competitionController;
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
    void getCompetitionShouldReturnCompetition() throws Exception {
        this.mockMvc
                .perform(get("/competitions/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name").value("FIFA World Cup"))
                .andExpect(jsonPath("$.events[0].name").value("Senegal vs Netherlands"));
    }

    @Test
    void getCompetitionShouldReturnException() throws Exception {
        this.mockMvc
                .perform(get("/competitions/100")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andExpect(result -> assertEquals("Competition with ID=100 doesn't exist.",
                        result.getResolvedException().getMessage()));
    }

    @Test
    void getAllCompetitionsShouldReturnCompetitions() throws Exception {
        this.mockMvc.perform(get("/competitions")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.[1].name").value("UEFA Champions League"));
    }

    @Test
    void addCompetitionShouldReturnCompetition() throws Exception {
        CompetitionDTO competitionDTO = CompetitionDTO
                .builder()
                .name("UEFA Futsal Euro 2022")
                .build();

        this.mockMvc
                .perform(post("/sports/1/competitions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(competitionDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("UEFA Futsal Euro 2022"));
    }


    @Test
    void addCompetitionShouldReturnExceptionCompetitionAlreadyExists() throws Exception {
        CompetitionDTO competitionDTO = CompetitionDTO
                .builder()
                .name("FIFA World Cup")
                .build();


        this.mockMvc.perform(post("/sports/1/competitions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(competitionDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isMethodNotAllowed())
                .andExpect(result -> assertEquals("Competition with name=FIFA World Cup already exists.",
                        result.getResolvedException().getMessage()));
    }

    @Test
    void addCompetitionShouldReturnExceptionNoCompetitionExists() throws Exception {
        CompetitionDTO competitionDTO = CompetitionDTO
                .builder()
                .name("UEFA Futsal Euro 2022")
                .build();

        this.mockMvc.perform(post("/sports/100/competitions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(competitionDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals("Sport with ID=100 doesn't exist.",
                        result.getResolvedException().getMessage()));
    }



    @Test
    void updateCompetitionShouldReturnCompetition() throws Exception {
        CompetitionDTO competitionDTO = CompetitionDTO
                .builder()
                .name("UEFA Futsal Euro 2022")
                .build();

        this.mockMvc
                .perform(put("/competitions/4")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(competitionDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(4))
                .andExpect(jsonPath("$.name").value("UEFA Futsal Euro 2022"));
    }

    @Test
    void updateCompetitionShouldReturnException() throws Exception {
        CompetitionDTO competitionDTO = CompetitionDTO
                .builder()
                .name("UEFA Futsal Euro 2022")
                .build();

        this.mockMvc
                .perform(put("/competitions/100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(competitionDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andExpect(result -> assertEquals("Competition with ID=100 doesn't exist.",
                        result.getResolvedException().getMessage()));
    }

    @Test
    void deleteCompetition() throws Exception {
        this.mockMvc.perform(delete("/competitions/5"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void deleteCompetitionShouldReturnException() throws Exception {
        this.mockMvc.perform(delete("/competitions/100"))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andExpect(result -> assertEquals("Competition with ID=100 doesn't exist.",
                        result.getResolvedException().getMessage()));
    }
}