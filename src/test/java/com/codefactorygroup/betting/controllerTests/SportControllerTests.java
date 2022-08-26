package com.codefactorygroup.betting.controllerTests;

import com.codefactorygroup.betting.controller.SportController;
import com.codefactorygroup.betting.dto.SportDTO;
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
class SportControllerTests {
    @Autowired
    private SportController sportController;
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
    void getSportShouldReturnSport() throws Exception {
        this.mockMvc.perform(get("/sports/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name").value("Football"))
                .andExpect(jsonPath("$.competitions[0].name").value("FIFA World Cup"));
    }

    @Test
    void getSportShouldReturnException() throws Exception {
        this.mockMvc.perform(get("/sports/100")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andExpect(result -> assertEquals("Sport with ID=100 doesn't exist.", result.getResolvedException().getMessage()));
    }

    @Test
    void getAllSportsShouldReturnSports() throws Exception {
        this.mockMvc.perform(get("/sports")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.[0].competitions[0].name").value("FIFA World Cup"))
                .andExpect(jsonPath("$.[4].name").value("Volleyball"));
    }

    @Test
    void addSportShouldReturnSport() throws Exception {
        SportDTO sportDTO = SportDTO.builder()
                .name("Golf")
                .build();

        this.mockMvc.perform(post("/sports")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(sportDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Golf"));
    }

    @Test
    void addSportShouldReturnException() throws Exception {
        SportDTO sportDTO = SportDTO.builder()
                .name("Football")
                .build();

        this.mockMvc.perform(post("/sports")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(sportDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(result -> assertEquals(String.format("Sport with name=%s already exists.", sportDTO.name()),
                        result.getResolvedException().getMessage()));
    }

    @Test
    void updateSportShouldReturnSport() throws Exception {
        SportDTO sportDTO = SportDTO.builder()
                .name("Tennis")
                .build();

        this.mockMvc.perform(put("/sports/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(sportDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name").value("Tennis"))
                .andExpect(jsonPath("$.competitions[0].name").value("FIFA World Cup"));
    }

    @Test
    void updateSportShouldReturnException() throws Exception {
        SportDTO sportDTO = SportDTO.builder()
                .name("Tennis")
                .build();

        this.mockMvc.perform(put("/sports/100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(sportDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andExpect(result -> assertEquals("Sport with ID=100 doesn't exist.", result.getResolvedException().getMessage()));
    }

    @Test
    void deleteSport() throws Exception {
        this.mockMvc.perform(delete("/sports/6"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void deleteSportShouldReturnException() throws Exception {
        this.mockMvc.perform(delete("/sports/100"))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals("Sport with ID=100 doesn't exist.",
                        result.getResolvedException().getMessage()));
    }

}
