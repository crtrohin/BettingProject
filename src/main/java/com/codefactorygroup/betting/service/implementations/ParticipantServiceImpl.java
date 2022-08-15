package com.codefactorygroup.betting.service.implementations;

import com.codefactorygroup.betting.converter.ParticipantDtoToParticipantConverter;
import com.codefactorygroup.betting.domain.Event;
import com.codefactorygroup.betting.domain.Participant;
import com.codefactorygroup.betting.dto.EventDTO;
import com.codefactorygroup.betting.dto.ParticipantDTO;
import com.codefactorygroup.betting.dto.RandomParticipantsDTO;
import com.codefactorygroup.betting.exception.NoSuchEntityExistsException;
import com.codefactorygroup.betting.repository.ParticipantRepository;
import com.codefactorygroup.betting.service.EventService;
import com.codefactorygroup.betting.service.ParticipantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component(value = "participantService")
@RequiredArgsConstructor
public class ParticipantServiceImpl implements ParticipantService {
    private final ParticipantDtoToParticipantConverter participantDtoToParticipantConverter;
    private final ParticipantRepository participantRepository;
    private final EventService eventService;

    @Transactional
    public ParticipantDTO getParticipant(final Integer participantId) {
        return participantRepository.findById(participantId)
                .map(ParticipantDTO::converter)
                .orElseThrow(() -> new NoSuchEntityExistsException(String.format("Participant with ID = %d doesn't exist.", participantId)));

    }

    @Transactional
    public List<ParticipantDTO> getAllParticipants() {
        return participantRepository
                .findAll()
                .stream()
                .map(ParticipantDTO::converter)
                .toList();
    }

    @Transactional
    public ParticipantDTO addParticipant(final ParticipantDTO newParticipant) {
        List<EventDTO> eventDTOS = Optional
                .of(newParticipant)
                .map(ParticipantDTO::events)
                .orElseGet(Collections::emptyList);


        List<Event> events = eventDTOS
                .stream()
                .map(eventDTO -> eventService.findEventByNameAndStartTimeAndEndTime(eventDTO.name(), eventDTO.startTime(),
                        eventDTO.endTime(), Optional.of(eventDTO)
                                .map(EventDTO::participants)
                                .orElseGet(Collections::emptyList)
                                .stream()
                                .map(participantDTO -> findParticipantByName(participantDTO.name()))
                                .collect(Collectors.toList())))
                .collect(Collectors.toList());

        Participant participant = participantDtoToParticipantConverter.convert(newParticipant);

        participant.setEvents(events);

        return ParticipantDTO.converter(participantRepository.save(participant));
    }

    @Transactional
    public void deleteParticipant(final Integer participantId) {
        participantRepository.deleteById(participantId);
    }

    private Participant update(final Participant participant, final ParticipantDTO toUpdateParticipant) {
        participant.setName(toUpdateParticipant.name());
        return participant;
    }

    @Transactional
    public ParticipantDTO updateParticipant(final ParticipantDTO toUpdateParticipant,
                                            final Integer participantId) {
        return participantRepository
                .findById(participantId)
                .map(participantFromDb -> update(participantFromDb, toUpdateParticipant))
                .map(participantRepository::save)
                .map(ParticipantDTO::converter)
                .orElseThrow(() -> new NoSuchEntityExistsException(String.format("Participant with ID = %d doesn't exist.", participantId)));
    }

    @Transactional
    public RandomParticipantsDTO getRandomParticipants() {
        List <ParticipantDTO> participantDTOS = participantRepository.findRandomParticipantsDTO(2)
                .stream()
                .map(ParticipantDTO::converter).toList();
        if (participantDTOS.size() != 2) {
            throw new RuntimeException("Not enough participants.");
        }
        return new RandomParticipantsDTO(participantDTOS.get(0), participantDTOS.get(1));
    }

    @Transactional
    public List<ParticipantDTO> findPaginated(int pageNo, int pageSize) {
        PageRequest paging = PageRequest.of(--pageNo, pageSize);
        Page<Participant> allParticipants = participantRepository.findAll(paging);

        return allParticipants.toList()
                .stream()
                .map(ParticipantDTO::converter)
                .toList();
    }

    @Transactional
    public Participant findParticipantByName(String name) {
        Optional<Participant> participantByNameOptional = participantRepository.findByName(name);
        if (participantByNameOptional.isPresent()) {
            return participantByNameOptional.get();
        } else {
            Participant newParticipant = new Participant();
            newParticipant.setName(name);
            return newParticipant;
        }
    }

}
