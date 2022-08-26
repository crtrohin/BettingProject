package com.codefactorygroup.betting.serviceTests;

import com.codefactorygroup.betting.converter.EventDTOtoEventConverter;
import com.codefactorygroup.betting.domain.Event;
import com.codefactorygroup.betting.domain.Competition;
import com.codefactorygroup.betting.dto.EventDTO;
import com.codefactorygroup.betting.exception.EntityAlreadyExistsException;
import com.codefactorygroup.betting.exception.NoSuchEntityExistsException;
import com.codefactorygroup.betting.repository.EventRepository;
import com.codefactorygroup.betting.repository.CompetitionRepository;
import com.codefactorygroup.betting.service.implementations.EventServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventServiceTests {

    @Mock
    private EventDTOtoEventConverter eventDtoToEventConverter;
    @Mock
    private EventRepository eventRepository;

    @Mock
    private CompetitionRepository competitionRepository;

    @InjectMocks
    private EventServiceImpl eventService;

    @Test
    void getEventShouldReturnEvent() {
        Optional<Event> optionalEvent = Optional.of(Event
                .builder()
                .id(10)
                .name("Real Madrid vs Liverpool")
                .build());

        when(eventRepository.findById(10)).thenReturn(optionalEvent);

        EventDTO eventDTO = eventService.getEvent(10);
        Event event = optionalEvent.get();

        assertThat(eventDTO).isNotNull();
        assertEquals(event.getId(), eventDTO.id());
        assertEquals(event.getName(), eventDTO.name());
    }

    @Test
    void getEventShouldReturnException() {
        when(eventRepository.findById(10)).thenReturn(Optional.empty());

        assertThrows(NoSuchEntityExistsException.class, () -> eventService.getEvent(10));
    }

    @Test
    void getAllEventsShouldReturnEvents() {
        Event event1 = Event
                .builder()
                .id(1)
                .name("FC Zurich vs PSV")
                .build();

        Event event2 = Event
                .builder()
                .id(2)
                .name("Copenhagen vs Dynamo Kyiv")
                .build();

        Event event3 = Event
                .builder()
                .id(3)
                .name("Dinamo Zagreb vs Monaco")
                .build();

        List<Event> events = List.of(event1, event2, event3);

        when(eventRepository.findAll()).thenReturn(events);

        List<EventDTO> eventDTOS = eventService.getAllEvents();

        assertEquals(eventDTOS.get(0).id(), event1.getId());
        assertEquals(eventDTOS.get(0).name(), event1.getName());

        assertEquals(eventDTOS.get(1).id(), event2.getId());
        assertEquals(eventDTOS.get(1).name(), event2.getName());

        assertEquals(eventDTOS.get(2).id(), event3.getId());
        assertEquals(eventDTOS.get(2).name(), event3.getName());
    }


    @Test
    void addEventShouldReturnEvent() {
        EventDTO eventDTO = EventDTO
                .builder()
                .id(1)
                .name("Dortmund vs Bayern")
                .startTime("Aug 27, 16:30")
                .endTime("Aug 27, 18:00")
                .participants(Collections.emptyList())
                .markets(Collections.emptyList())
                .build();

        Event event1 = Event
                .builder()
                .id(1)
                .name("Dortmund vs Bayern")
                .startTime("Aug 27, 16:30")
                .endTime("Aug 27, 18:00")
                .participants(Collections.emptyList())
                .markets(Collections.emptyList())
                .build();

        Event event2 = Event
                .builder()
                .id(2)
                .name("VFL Bochum vs Koln")
                .startTime("Aug 31, 14:30")
                .endTime("Aug 31, 16:00")
                .build();

        List<Event> events = new ArrayList<>();
        events.add(event2);

        Competition competition = Competition
                .builder()
                .id(1)
                .name("Bundesliga")
                .events(events)
                .build();

        when(competitionRepository.findById(1)).thenReturn(Optional.of(competition));
        when(eventRepository.existsEventByCompetitionIdAndNameAndStartTimeAndEndTime(1, "Dortmund vs Bayern",
                "Aug 27, 16:30", "Aug 27, 18:00")).thenReturn(false);
        when(eventDtoToEventConverter.convert(eventDTO)).thenReturn(event1);
        when(eventRepository.save(event1)).thenReturn(event1);

        EventDTO savedEvent = eventService.addEvent(1, eventDTO);

        assertEquals(eventDTO, savedEvent);
    }

    @Test
    void addEventShouldReturnExceptionNoCompetitionExists() {
        EventDTO eventDTO = EventDTO
                .builder()
                .id(1)
                .name("Dortmund vs Bayern")
                .startTime("Aug 27, 16:30")
                .endTime("Aug 27, 18:00")
                .participants(Collections.emptyList())
                .markets(Collections.emptyList())
                .build();

        when(competitionRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(NoSuchEntityExistsException.class, () -> eventService.addEvent(1, eventDTO));
    }

    @Test
    void addEventShouldReturnExceptionEventAlreadyExists() {
        Event event = Event
                .builder()
                .id(1)
                .name("Dortmund vs Bayern")
                .startTime("Aug 27, 16:30")
                .endTime("Aug 27, 18:00")
                .participants(Collections.emptyList())
                .markets(Collections.emptyList())
                .build();

        EventDTO eventDTO = EventDTO
                .builder()
                .id(1)
                .name("Dortmund vs Bayern")
                .startTime("Aug 27, 16:30")
                .endTime("Aug 27, 18:00")
                .participants(Collections.emptyList())
                .markets(Collections.emptyList())
                .build();

        List<Event> events = List.of(event);

        Competition competition = Competition
                .builder()
                .id(1)
                .name("Bundesliga")
                .events(events)
                .build();

        when(competitionRepository.findById(1)).thenReturn(Optional.of(competition));
        when(eventRepository.existsEventByCompetitionIdAndNameAndStartTimeAndEndTime(1, "Dortmund vs Bayern",
                "Aug 27, 16:30", "Aug 27, 18:00")).thenReturn(true);

        assertThrows(EntityAlreadyExistsException.class, () -> eventService.addEvent(1, eventDTO));
    }

    @Test
    void updateEventShouldReturnEvent() {
        Event eventFromDb = Event
                .builder()
                .id(1)
                .name("Dortmund vs Bayern")
                .startTime("Aug 27, 16:30")
                .endTime("Aug 27, 18:00")
                .participants(Collections.emptyList())
                .markets(Collections.emptyList())
                .build();

        Event updatedEvent = Event
                .builder()
                .id(1)
                .name("VFL Bochum vs Koln")
                .startTime("Aug 31, 14:30")
                .endTime("Aug 31, 16:00")
                .participants(Collections.emptyList())
                .markets(Collections.emptyList())
                .build();

        EventDTO toUpdateEventDTO = EventDTO
                .builder()
                .id(1)
                .name("VFL Bochum vs Koln")
                .startTime("Aug 31, 14:30")
                .endTime("Aug 31, 16:00")
                .participants(Collections.emptyList())
                .markets(Collections.emptyList())
                .build();

        when(eventRepository.findById(1)).thenReturn(Optional.of(eventFromDb));
        when(eventRepository.save(updatedEvent)).thenReturn(updatedEvent);

        EventDTO resultedEventDTO = eventService.updateEvent(toUpdateEventDTO, 1);

        assertEquals(resultedEventDTO, toUpdateEventDTO);
    }

    @Test
    void updateEventShouldReturnException() {
        EventDTO toUpdateEventDTO = EventDTO
                .builder()
                .id(1)
                .name("Arsenal vs Real Madrid")
                .build();

        when(eventRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(NoSuchEntityExistsException.class, () -> eventService.updateEvent(toUpdateEventDTO, 1));
    }


    @Test
    void deleteEvent() {
        when(eventRepository.existsById(1)).thenReturn(true);

        eventService.deleteEvent(1);

        // check if the method was called
        verify(eventRepository).deleteById(1);
    }


    @Test
    void deleteEventShouldReturnException() {
        when(eventRepository.existsById(1)).thenReturn(false);

        assertThrows(NoSuchEntityExistsException.class, () -> eventService.deleteEvent(1));
    }

}
