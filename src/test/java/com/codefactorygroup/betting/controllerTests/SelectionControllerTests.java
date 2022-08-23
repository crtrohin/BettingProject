package com.codefactorygroup.betting.controllerTests;

import com.codefactorygroup.betting.controller.SelectionController;
import com.codefactorygroup.betting.dto.SelectionDTO;
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
class SelectionControllerTests {
    @Autowired
    private SelectionController selectionController;
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
    void getSelectionShouldReturnSelection() throws Exception {
        this.mockMvc.perform(get("/selections/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name").value("Senegal"));
    }

    @Test
    void getSelectionShouldReturnException() throws Exception {
        this.mockMvc.perform(get("/selections/100")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andExpect(result -> assertEquals("Selection with ID=100 doesn't exist.",
                        result.getResolvedException().getMessage()));
    }

    @Test
    void getAllSelectionsShouldReturnSelections() throws Exception {
        this.mockMvc.perform(get("/selections")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].name").value("Senegal"));
    }

    @Test
    void addSelectionShouldReturnSelection() throws Exception {
        SelectionDTO selectionDTO = SelectionDTO.builder()
                .name("Arsenal")
                .odds(2)
                .build();

        this.mockMvc.perform(post("/markets/1/selections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(selectionDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Arsenal"))
                .andExpect(jsonPath("$.odds").value(2));
    }

    @Test
    void addSelectionShouldReturnExceptionNameAlreadyExists() throws Exception {
        SelectionDTO selectionDTO = SelectionDTO.builder()
                .name("Draw")
                .odds(2)
                .build();

        this.mockMvc.perform(post("/markets/1/selections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(selectionDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(result -> assertEquals("Selection with name=Draw already exists.", result.getResolvedException().getMessage()));
    }

    @Test
    void addSelectionShouldReturnException() throws Exception {
        SelectionDTO selectionDTO = SelectionDTO.builder()
                .name("Draw")
                .odds(2)
                .build();

        this.mockMvc.perform(post("/markets/100/selections")
                        .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(selectionDTO))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals("Market with ID=100 doesn't exist.",
                        result.getResolvedException().getMessage()));
    }


    @Test
    void updateSelectionShouldReturnSelection() throws Exception {
        SelectionDTO selectionDTO = SelectionDTO.builder()
                .name("Arsenal")
                .odds(10)
                .build();

        this.mockMvc.perform(put("/selections/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(selectionDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.odds").value(10));
    }

    @Test
    void updateSelectionShouldReturnException() throws Exception {
        SelectionDTO selectionDTO = SelectionDTO.builder()
                .name("Real Madrid")
                .odds(2)
                .build();

        this.mockMvc.perform(put("/selections/100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(selectionDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andExpect(result -> assertEquals("Selection with ID=100 doesn't exist.", result.getResolvedException().getMessage()));
    }

    @Test
    void deleteSelection() throws Exception {
        this.mockMvc.perform(delete("/selections/10"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void deleteSelectionShouldReturnException() throws Exception {
        this.mockMvc.perform(delete("/selections/100"))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals("Selection with ID=100 doesn't exist.",
                        result.getResolvedException().getMessage()));
    }
}
