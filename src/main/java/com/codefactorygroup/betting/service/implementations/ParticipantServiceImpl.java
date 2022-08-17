package com.codefactorygroup.betting.service.implementations;

import com.codefactorygroup.betting.converter.ParticipantDtoToParticipantConverter;
import com.codefactorygroup.betting.domain.Participant;
import com.codefactorygroup.betting.dto.ParticipantDTO;
import com.codefactorygroup.betting.dto.RandomParticipantsDTO;
import com.codefactorygroup.betting.exception.EntityAlreadyExistsException;
import com.codefactorygroup.betting.exception.NoSuchEntityExistsException;
import com.codefactorygroup.betting.repository.ParticipantRepository;
import com.codefactorygroup.betting.service.ParticipantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service(value = "participantService")
@RequiredArgsConstructor
public class ParticipantServiceImpl implements ParticipantService {
    private final ParticipantDtoToParticipantConverter participantDtoToParticipantConverter;
    private final ParticipantRepository participantRepository;

    @Override
    public ParticipantDTO getParticipant(final Integer participantId) {
        return participantRepository.findById(participantId)
                .map(ParticipantDTO::converter)
                .orElseThrow(() -> new NoSuchEntityExistsException(String.format("No participant with ID=%d was found.", participantId)));

    }

    @Override
    public List<ParticipantDTO> getAllParticipants() {
        return participantRepository.findAll()
                .stream()
                .map(ParticipantDTO::converter)
                .collect(Collectors.toList());
    }

    @Override
    public List<ParticipantDTO> getParticipantsByEventId(Integer eventId) {
        return participantRepository.findParticipantsByEventId(eventId)
                .stream()
                .map(ParticipantDTO::converter)
                .collect(Collectors.toList());
    }

    @Override
    public RandomParticipantsDTO getRandomParticipants() {
        List <ParticipantDTO> participantDTOS = participantRepository.findRandomParticipantsDTO(2)
                .stream()
                .map(ParticipantDTO::converter).toList();
        if (participantDTOS.size() != 2) {
            throw new RuntimeException("Not enough participants.");
        }
        return new RandomParticipantsDTO(participantDTOS.get(0), participantDTOS.get(1));
    }

    @Override
    public List<ParticipantDTO> findPaginated(int pageNo, int pageSize) {
        PageRequest paging = PageRequest.of(--pageNo, pageSize);
        Page<Participant> allParticipants = participantRepository.findAll(paging);

        return allParticipants.toList()
                .stream()
                .map(ParticipantDTO::converter)
                .toList();
    }

    @Override
    @Transactional
    public ParticipantDTO addParticipant(final ParticipantDTO newParticipant) {
        Optional<Participant> optionalParticipant = participantRepository.findByName(newParticipant.name());
        if (optionalParticipant.isPresent()) {
            throw new EntityAlreadyExistsException(String.format("Participant with name=%s already exists.", newParticipant.name()));
        }
        return ParticipantDTO.converter(participantRepository.
                save(participantDtoToParticipantConverter.convert(newParticipant)));
    }

    @Override
    @Transactional
    public void deleteParticipant(final Integer participantId) {
        boolean participantExists = participantRepository.existsById(participantId);
        if (participantExists) {
            participantRepository.deleteById(participantId);
        } else {
            throw new NoSuchEntityExistsException(String.format("Participant with ID=%d doesn't exist.", participantId));
        }
    }

    private Participant update(final Participant participant, final ParticipantDTO toUpdateParticipant) {
        participant.setName(toUpdateParticipant.name());
        return participant;
    }

    @Override
    @Transactional
    public ParticipantDTO updateParticipant(final ParticipantDTO toUpdateParticipant,
                                            final Integer participantId) {
        return participantRepository
                .findById(participantId)
                .map(participantFromDb -> update(participantFromDb, toUpdateParticipant))
                .map(participantRepository::save)
                .map(ParticipantDTO::converter)
                .orElseThrow(() -> new NoSuchEntityExistsException(String.format("No participant with ID=%d was found.", participantId)));
    }

}
