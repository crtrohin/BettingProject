package com.codefactorygroup.betting.controllerTests;

import com.codefactorygroup.betting.controller.SportController;
import com.codefactorygroup.betting.dto.CompetitionDTO;
import com.codefactorygroup.betting.dto.EventDTO;
import com.codefactorygroup.betting.dto.SportDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

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
                .andExpect(jsonPath("$.name").value("Footbal"));
    }

    @Test
    void getSportShouldReturnException() throws Exception {
        this.mockMvc.perform(get("/sports/100")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print())
				.andExpect(result -> assertEquals("Sport with ID = 100 doesn't exist.", result.getResolvedException().getMessage()));
    }

    @Test
    void addSportShouldReturnSport() throws Exception {
        List<EventDTO> events = new ArrayList<>() {{
            add(new EventDTO(100, "Arsenal vs Barcelona", null, null, null, null));
        }};
        List<CompetitionDTO> competitions = new ArrayList<>() {{
            add(new CompetitionDTO(100, "Champions League", events));
        }};
        SportDTO sportDTO = new SportDTO(10, "Football", competitions);

        this.mockMvc.perform(post("/sports")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(sportDTO))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Football"));
    }

    @Test
    void addSportShoudlReturnException() throws Exception {
        List<EventDTO> events = new ArrayList<>() {{
            add(new EventDTO(1, "Arsenal vs Barcelona", null, null, null, null));
        }};
        List<CompetitionDTO> competitions = new ArrayList<>() {{
            add(new CompetitionDTO(1, "Arsenal", events));
        }};
        SportDTO sportDTO = new SportDTO(1, "Champions League", competitions);

        this.mockMvc.perform(post("/sports")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(sportDTO))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(result -> assertEquals("Sport with ID = 1 already exists.", result.getResolvedException().getMessage()));
    }

    @Test
    void updateSportShouldReturnSport() throws Exception {
        List<EventDTO> events = new ArrayList<>() {{
            add(new EventDTO(1, "Arsenal vs Barcelona", null, null, null, null));
        }};
        List<CompetitionDTO> competitions = new ArrayList<>() {{
            add(new CompetitionDTO(100, "Champions League", events));
        }};
        SportDTO sportDTO = new SportDTO(1, "Football", competitions);
        this.mockMvc.perform(put("/sports/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(sportDTO))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.competitions[0].name").value("FIFA World Cup"));
    }

    @Test
    void updateSportShouldReturnException() throws Exception {
        List<EventDTO> events = new ArrayList<>() {{
            add(new EventDTO(1, "Arsenal vs Barcelona", null, null, null, null));
        }};
        List<CompetitionDTO> competitions = new ArrayList<>() {{
            add(new CompetitionDTO(1, "Arsenal", events));
        }};
        SportDTO sportDTO = new SportDTO(1, "Champions League", competitions);
        this.mockMvc.perform(put("/sports/100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(sportDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andExpect(result -> assertEquals("Sport with ID = 100 doesn't exist.", result.getResolvedException().getMessage()));
    }

}
