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
                .andExpect(result -> assertEquals("Selection with ID = 100 doesn't exist.", result.getResolvedException().getMessage()));
    }

    @Test
    void addSelectionShouldReturnSelection() throws Exception {
        SelectionDTO selectionDTO = new SelectionDTO(100, "Arsenal", 2);

        this.mockMvc.perform(post("/selections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(selectionDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Arsenal"));
    }

    @Test
    void addSelectionShoudlReturnException() throws Exception {
        SelectionDTO selectionDTO = new SelectionDTO(1, "Arsenal", 2);

        this.mockMvc.perform(post("/selections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(selectionDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(result -> assertEquals("Selection with ID = 1 already exists.", result.getResolvedException().getMessage()));
    }


    @Test
    void updateSelectionShouldReturnSelection() throws Exception {
        SelectionDTO selectionDTO = new SelectionDTO(1, "Arsenal", 2);
        this.mockMvc.perform(put("/selections/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(selectionDTO))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.odds").value(2));
    }

    @Test
    void updateSelectionShouldReturnException() throws Exception {
        SelectionDTO selectionDTO = new SelectionDTO(100, "Champions League", 2);
        this.mockMvc.perform(put("/selections/100")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(selectionDTO))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andExpect(result -> assertEquals("Selection with ID = 100 doesn't exist.", result.getResolvedException().getMessage()));
    }
}
