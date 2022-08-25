package com.codefactorygroup.betting.controllerTests;

import com.codefactorygroup.betting.controller.EventController;
import com.codefactorygroup.betting.dto.EventDTO;
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
class EventControllerTests {
    @Autowired
    private EventController eventController;
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
    void getEventShouldReturnEvent() throws Exception {
        this.mockMvc
                .perform(get("/events/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name").value("Senegal vs Netherlands"))
                .andExpect(jsonPath("$.markets[0].selections[0].name").value("Senegal"))
                .andExpect(jsonPath("$.participants[0].name").value("Senegal"));
    }

    @Test
    void getEventShouldReturnException() throws Exception {
        this.mockMvc
                .perform(get("/events/100")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andExpect(result -> assertEquals("Event with ID=100 doesn't exist.", result.getResolvedException().getMessage()));
    }

    @Test
    void getAllEventsShouldReturnEvents() throws Exception {
        this.mockMvc.perform(get("/events")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.[1].name").value("Qatar vs Ecuador"));
    }

    @Test
    void getEventsByCompetitionIdShouldReturnEvent() throws Exception {
        this.mockMvc.perform(get("/competitions/1/events")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.[0].name").value("Senegal vs Netherlands"));
    }

    @Test
    void getEventsByCompetitionIdShouldReturnException() throws Exception {
        this.mockMvc.perform(get("/competitions/100/events")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andExpect(result -> assertEquals("Competition with ID=100 doesn't exist.",
                        result.getResolvedException().getMessage()));
    }

    @Test
    void getEventsByParticipantIdShouldReturnEvent() throws Exception {
        this.mockMvc.perform(get("/participants/31/events")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.[0].name").value("Senegal vs Netherlands"));
    }

    @Test
    void getEventsByParticipantIdShouldReturnException() throws Exception {
        this.mockMvc.perform(get("/participants/100/events")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andExpect(result -> assertEquals("Participant with ID=100 doesn't exist.",
                        result.getResolvedException().getMessage()));
    }

    @Test
    void getEventsByMarketIdShouldReturnEvent() throws Exception {
        this.mockMvc.perform(get("/markets/1/events")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.[0].name").value("Senegal vs Netherlands"));
    }

    @Test
    void getEventsByMarketIdShouldReturnException() throws Exception {
        this.mockMvc.perform(get("/markets/100/events")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andExpect(result -> assertEquals("Market with ID=100 doesn't exist.",
                        result.getResolvedException().getMessage()));
    }


    @Test
    void addEventShouldReturnEvent() throws Exception {
        EventDTO eventDTO = EventDTO
                .builder()
                .name("AC Milan vs Paris Saint-German")
                .startTime("12/02/2023 14:00")
                .endTime("12/02/2023 15:30")
                .build();

        this.mockMvc
                .perform(post("/competitions/1/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(eventDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.startTime").value("12/02/2023 14:00"));
    }


    @Test
    void addEventShouldReturnExceptionEventAlreadyExists() throws Exception {
        EventDTO eventDTO = EventDTO
                .builder()
                .name("Uruguay vs South Korea")
                .startTime("12/02/2023 14:00")
                .endTime("12/02/2023 15:30")
                .build();


        this.mockMvc.perform(post("/competitions/1/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(eventDTO))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isMethodNotAllowed())
                .andExpect(result -> assertEquals("Event with name=Uruguay vs South Korea, startTime=12/02/2023 14:00, " +
                                "endTime=12/02/2023 15:30 already exists.",
                        result.getResolvedException().getMessage()));
    }

    @Test
    void addEventShouldReturnExceptionNoCompetitionExists() throws Exception {
        EventDTO eventDTO = EventDTO
                .builder()
                .name("AC Milan vs Paris Saint-German")
                .startTime("12/02/2023 14:00")
                .endTime("12/02/2023 15:30")
                .build();

        this.mockMvc.perform(post("/competitions/100/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(eventDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals("Competition with ID=100 doesn't exist.",
                        result.getResolvedException().getMessage()));
    }


    @Test
    void addParticipantToEventShouldReturnEvent() throws Exception {
        this.mockMvc.perform(put("/events/2/participants/2"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.participants[0].name").value("Qatar"))
                .andExpect(jsonPath("$.participants[2].name").value("Real Madrid"))
                .andExpect(jsonPath("$.participants[2].id").value(2));
    }

    @Test
    void addParticipantToEventShouldReturnExceptionNoEventExists() throws Exception {
        this.mockMvc.perform(put("/events/100/participants/3"))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andExpect(result -> assertEquals("Event with ID=100 doesn't exist.",
                        result.getResolvedException().getMessage()));
    }

    @Test
    void addParticipantToEventShouldReturnExceptionNoParticipantsExists() throws Exception {
        this.mockMvc.perform(put("/events/1/participants/100"))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andExpect(result -> assertEquals("Participant with ID=100 doesn't exist.",
                        result.getResolvedException().getMessage()));
    }

    @Test
    void addParticipantToEventShouldReturnExceptionParticipantAlreadyLinked() throws Exception {
        this.mockMvc.perform(put("/events/1/participants/31"))
                .andExpect(status().isMethodNotAllowed())
                .andDo(print())
                .andExpect(result -> assertEquals("Participant with ID=31 is already linked to event with ID=1.",
                        result.getResolvedException().getMessage()));
    }


    @Test
    void updateEventShouldReturnEvent() throws Exception {
        EventDTO eventDTO = EventDTO
                .builder()
                .name("Uruguay vs South Korea")
                .startTime("12/02/2023 14:00")
                .endTime("12/02/2023 15:30")
                .build();

        this.mockMvc
                .perform(put("/events/11")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(eventDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(11))
                .andExpect(jsonPath("$.name").value("Uruguay vs South Korea"))
                .andExpect(jsonPath("$.participants[1].name").value("Croatia"));
    }

    @Test
    void updateEventShouldReturnException() throws Exception {
        EventDTO eventDTO = EventDTO
                .builder()
                .name("AC Milan vs Paris Saint-German")
                .startTime("12/02/2023 14:00")
                .endTime("12/02/2023 15:30")
                .build();

        this.mockMvc
                .perform(put("/events/100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(eventDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andExpect(result -> assertEquals("Event with ID=100 doesn't exist.",
                        result.getResolvedException().getMessage()));
    }

    @Test
    void deleteEvent() throws Exception {
        this.mockMvc.perform(delete("/events/10"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void deleteEventShouldReturnException() throws Exception {
        this.mockMvc.perform(delete("/events/100"))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andExpect(result -> assertEquals("Event with ID=100 doesn't exist.",
                        result.getResolvedException().getMessage()));
    }
}