package com.codefactorygroup.betting.controllerTests;

import com.codefactorygroup.betting.controller.EventController;
import com.codefactorygroup.betting.dto.EventDTO;
import com.codefactorygroup.betting.dto.MarketDTO;
import com.codefactorygroup.betting.dto.ParticipantDTO;
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
                .andExpect(jsonPath("$.name").value("Senegal vs Netherlands"));
    }

    @Test
    void getEventShouldReturnException() throws Exception {
        this.mockMvc
                .perform(get("/events/100")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andExpect(result -> assertEquals("Event with ID = 100 doesn't exist.", result.getResolvedException().getMessage()));
    }

    @Test
    void addEventShouldReturnEvent() throws Exception {
        List<ParticipantDTO> participants = new ArrayList<>() {{
            add(new ParticipantDTO(1, "AC Milan"));
            add(new ParticipantDTO(2, "Paris Saint-German"));
        }};
        List<SelectionDTO> selections = new ArrayList<>() {{
            add(new SelectionDTO(100, "AC Milan", 100));
            add(new SelectionDTO(101, "Draw", 10));
            add(new SelectionDTO(102, "Paris Saint-German", 5));
        }};
        List<MarketDTO> markets = new ArrayList<>() {{
            add(new MarketDTO(20, "1x2", selections));
            add(new MarketDTO(21, "Handicap", null));
        }};
        EventDTO eventDTO = new EventDTO(100, "AC Milan vs Paris Saint-German", participants, "12/02/2023 14:00", "12/02/2023 15:30", markets);

        this.mockMvc
                .perform(post("/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(eventDTO))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventParticipants[1].name").value("Paris Saint-German"));
    }
}
//
//    @Test
//    void addEventShoudlReturnException() throws Exception {
//        List<SelectionDTO> selections = new ArrayList<>() {{
//            add(new SelectionDTO(100, "Arsenal", 1 / 2));
//            add(new SelectionDTO(101, "Real Madrid", 1 / 3));
//            add(new SelectionDTO(102, "Draw", 12 / 1));
//        }};
//        EventDTO eventDTO = new EventDTO(1, "1x2", selections);
//
//        this.mockMvc.perform(post("/events")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(asJsonString(eventDTO))
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isMethodNotAllowed())
//                .andExpect(result -> assertEquals("Event with ID = 1 already exists.", result.getResolvedException().getMessage()));
//    }
//
//    @Test
//    void updateEventShouldReturnEvent() throws Exception {
//        List<SelectionDTO> selections = new ArrayList<>() {{
//            add(new SelectionDTO(100, "Arsenal", 1 / 2));
//            add(new SelectionDTO(101, "Real Madrid", 1 / 3));
//            add(new SelectionDTO(102, "Draw", 12 / 1));
//        }};
//        EventDTO eventDTO = new EventDTO(1, "1x2", selections);
//        this.mockMvc
//                .perform(put("/events/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(asJsonString(eventDTO))
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andDo(print())
//                .andExpect(jsonPath("$.selections[1].name").value("Draw"));
//    }
//
//    @Test
//    void updateEventShouldReturnException() throws Exception {
//        List<SelectionDTO> selections = new ArrayList<>() {{
//            add(new SelectionDTO(100, "Arsenal", 1 / 2));
//            add(new SelectionDTO(101, "Real Madrid", 1 / 3));
//            add(new SelectionDTO(102, "Draw", 12 / 1));
//        }};
//        EventDTO eventDTO = new EventDTO(100, "1x2", selections);
//        this.mockMvc
//                .perform(put("/events/100")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(asJsonString(eventDTO))
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNotFound())
//                .andDo(print())
//                .andExpect(result -> assertEquals("Event with ID = 100 doesn't exist.", result.getResolvedException().getMessage()));
//    }
//}
//package com.codefactorygroup.betting.controllerTests;
//
//import com.codefactorygroup.betting.controller.EventController;
//import com.codefactorygroup.betting.dto.EventDTO;
//import com.codefactorygroup.betting.dto.ParticipantDTO;
//import com.codefactorygroup.betting.exception.NoSuchEntityExistsException;
//import com.codefactorygroup.betting.exception.EntityAlreadyExistsException;
//import com.codefactorygroup.betting.service.EventService;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//class EventControllerTests {
//
//    private final EventService eventService = mock(EventService.class);
//    private final EventController eventController = new EventController(eventService);
//
//
//    @Test
//    void getEventShouldReturnEvent() {
//        List<ParticipantDTO> participants = new ArrayList<>() {{
//            add(new ParticipantDTO(1, "Arsenal"));
//        }};
//        EventDTO eventDTO = new EventDTO(1, "Champions League", participants, null, null, null);
//        when(eventService.getEvent(10)).thenReturn(eventDTO);
//
//        var response = eventController.getEvent(10);
//
//        assertEquals(eventDTO, response);
//    }
//
//    @Test
//    void getEventShouldReturnException() {
//        when(eventService.getEvent(10)).thenThrow(new NoSuchEntityExistsException(String.format("No event with ID = %d was found.", 10)));
//
//        NoSuchEntityExistsException noSuchEntityExistsException = assertThrows(NoSuchEntityExistsException.class,
//                () -> {eventController.getEvent(10);
//                });
//
//        assertEquals("No event with ID = 10 was found.", noSuchEntityExistsException.getMessage());
//    }
//
//    @Test
//    void addEventShouldReturnEvent() {
//        List<ParticipantDTO> participants = new ArrayList<>() {{
//            add(new ParticipantDTO(1, "Arsenal"));
//        }};
//        EventDTO eventDTO = new EventDTO(1, "Champions League", participants, null, null, null);
//        when(eventService.addEvent(eventDTO)).thenReturn(eventDTO);
//
//        var response = eventController.addEvent(eventDTO);
//
//        assertEquals(eventDTO, response);
//    }
//
//    @Test
//    void addEventShoudlReturnException() {
//        List<ParticipantDTO> participants = new ArrayList<>() {{
//            add(new ParticipantDTO(1, "Arsenal"));
//        }};
//        EventDTO eventDTO = new EventDTO(1, "Champions League", participants, null, null, null);
//        when(eventService.addEvent(eventDTO)).thenThrow(new EntityAlreadyExistsException(String.format("Event with ID = %d already exists.", 1)));
//
//        EntityAlreadyExistsException entityAlreadyExistsException = assertThrows(EntityAlreadyExistsException.class,
//                () -> eventController.addEvent(eventDTO));
//
//        assertEquals("Event with ID = 1 already exists.", entityAlreadyExistsException.getMessage());
//    }
//
//    @Test
//    void updateEventShouldReturnEvent() {
//        List<ParticipantDTO> participants = new ArrayList<>() {{
//            add(new ParticipantDTO(1, "Arsenal"));
//        }};
//        EventDTO eventDTO = new EventDTO(1, "Champions League", participants, null, null, null);
//        when(eventService.updateEvent(eventDTO, 1)).thenReturn(eventDTO);
//
//        EventDTO response = eventController.updateEvent(eventDTO, 1);
//
//        assertEquals(eventDTO, response);
//    }
//
//    @Test
//    void updateEventShouldReturnException() {
//        List<ParticipantDTO> participants = new ArrayList<>() {{
//            add(new ParticipantDTO(1, "Arsenal"));
//        }};
//        EventDTO eventDTO = new EventDTO(1, "Champions League", participants, null, null, null);
//        when(eventService.updateEvent(eventDTO, 1)).thenThrow(new NoSuchEntityExistsException(String.format("No participant with ID = %d was found.", 1)));
//
//        NoSuchEntityExistsException noSuchEntityExistsException = assertThrows(NoSuchEntityExistsException.class,
//                () -> eventController.updateEvent(eventDTO, 1));
//
//        assertEquals("No participant with ID = 1 was found.", noSuchEntityExistsException.getMessage());
//    }
//
//}
