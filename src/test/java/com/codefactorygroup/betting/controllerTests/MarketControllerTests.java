package com.codefactorygroup.betting.controllerTests;

import com.codefactorygroup.betting.controller.MarketController;
import com.codefactorygroup.betting.dto.MarketDTO;
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
                .perform(get("/markets/3")
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
                .andExpect(result -> assertEquals("Market with ID=100 doesn't exist.", result.getResolvedException().getMessage()));
    }

    @Test
    void getAllMarketsShouldReturnMarkets() throws Exception {
        this.mockMvc.perform(get("/markets")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].selections[0].name").value("Senegal"));
    }

    @Test
    void getMarketsByEventIdShouldReturnMarkets() throws Exception {
        this.mockMvc.perform(get("/events/1/markets")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.[0].name").value("1x2"));
    }

    @Test
    void getMarketsByEventIdShouldReturnException() throws Exception {
        this.mockMvc.perform(get("/events/100/markets")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andExpect(result -> assertEquals("Event with ID=100 doesn't exist.",
                        result.getResolvedException().getMessage()));
    }


    @Test
    void addMarketShouldReturnMarket() throws Exception {
        MarketDTO marketDTO = MarketDTO
                .builder()
                .name("Top 3")
                .build();

        this.mockMvc
                .perform(post("/events/1/markets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(marketDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Top 3"));
    }

    @Test
    void addMarketShouldReturnExceptionNoEventExists() throws Exception {
        MarketDTO marketDTO = MarketDTO
                .builder()
                .name("Top 3")
                .build();

        this.mockMvc.perform(post("/events/100/markets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(marketDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals("Event with ID=100 doesn't exist.",
                        result.getResolvedException().getMessage()));
    }

    @Test
    void addMarketShouldReturnExceptionMarketAlreadyExists() throws Exception {
        MarketDTO marketDTO = MarketDTO
                .builder()
                .name("1x2")
                .build();

        this.mockMvc.perform(post("/events/1/markets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(marketDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(result -> assertEquals("Market with name=1x2 already exists.",
                        result.getResolvedException().getMessage()));

    }

    @Test
    void updateMarketShouldReturnMarket() throws Exception {
        MarketDTO marketDTO = MarketDTO
                .builder()
                .name("Handicap")
                .build();

        this.mockMvc
                .perform(put("/markets/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(marketDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name").value("Handicap"))
                .andExpect(jsonPath("$.selections[2].name").value("Saudi Arabia"));
    }

    @Test
    void updateMarketShouldReturnException() throws Exception {
        MarketDTO marketDTO = MarketDTO
                .builder()
                .name("Top 3")
                .build();

        this.mockMvc
                .perform(put("/markets/100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(marketDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andExpect(result -> assertEquals("Market with ID=100 doesn't exist.", result.getResolvedException().getMessage()));
    }

    @Test
    void deleteMarket() throws Exception {
        this.mockMvc
                .perform(delete("/markets/2"))
                .andExpect(status().isOk())
                .andDo(print());
    }


    @Test
    void deleteMarketShouldReturnException() throws Exception {
        this.mockMvc
                .perform(delete("/markets/100"))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
}
