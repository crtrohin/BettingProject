package com.codefactorygroup.betting.controllerTests;

import com.codefactorygroup.betting.controller.MarketController;
import com.codefactorygroup.betting.dto.CompetitionDTO;
import com.codefactorygroup.betting.dto.EventDTO;
import com.codefactorygroup.betting.dto.MarketDTO;
import com.codefactorygroup.betting.dto.SelectionDTO;
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
class MarketControllerTests {
    @Autowired
    private MarketController marketController;
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
    void getMarketShouldReturnMarket() throws Exception {
        this.mockMvc
                .perform(get("/markets/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name").value("1x2"));
    }

    @Test
    void getMarketShouldReturnException() throws Exception {
        this.mockMvc
                .perform(get("/markets/100")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andExpect(result -> assertEquals("Market with ID = 100 doesn't exist.", result.getResolvedException().getMessage()));
    }

    @Test
    void addMarketShouldReturnMarket() throws Exception {
        List<SelectionDTO> selections = new ArrayList<>() {{
            add(new SelectionDTO(100, "Arsenal", 1 / 2));
            add(new SelectionDTO(101, "Real Madrid", 1 / 3));
            add(new SelectionDTO(102, "Draw", 12 / 1));
        }};
        MarketDTO marketDTO = new MarketDTO(100, "1x2", selections);

        this.mockMvc
                .perform(post("/markets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(marketDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.selections[2].name").value("Draw"));
    }

    @Test
    void addMarketShoudlReturnException() throws Exception {
        List<SelectionDTO> selections = new ArrayList<>() {{
            add(new SelectionDTO(100, "Arsenal", 1 / 2));
            add(new SelectionDTO(101, "Real Madrid", 1 / 3));
            add(new SelectionDTO(102, "Draw", 12 / 1));
        }};
        MarketDTO marketDTO = new MarketDTO(1, "1x2", selections);

        this.mockMvc.perform(post("/markets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(marketDTO))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(result -> assertEquals("Market with ID = 1 already exists.", result.getResolvedException().getMessage()));
    }

    @Test
    void updateMarketShouldReturnMarket() throws Exception {
        List<SelectionDTO> selections = new ArrayList<>() {{
            add(new SelectionDTO(100, "Arsenal", 1 / 2));
            add(new SelectionDTO(101, "Real Madrid", 1 / 3));
            add(new SelectionDTO(102, "Draw", 12 / 1));
        }};
        MarketDTO marketDTO = new MarketDTO(1, "1x2", selections);
        this.mockMvc
                .perform(put("/markets/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(marketDTO))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.selections[1].name").value("Draw"));
    }

    @Test
    void updateMarketShouldReturnException() throws Exception {
        List<SelectionDTO> selections = new ArrayList<>() {{
            add(new SelectionDTO(100, "Arsenal", 1 / 2));
            add(new SelectionDTO(101, "Real Madrid", 1 / 3));
            add(new SelectionDTO(102, "Draw", 12 / 1));
        }};
        MarketDTO marketDTO = new MarketDTO(100, "1x2", selections);
        this.mockMvc
                .perform(put("/markets/100")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(marketDTO))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andExpect(result -> assertEquals("Market with ID = 100 doesn't exist.", result.getResolvedException().getMessage()));
    }
}