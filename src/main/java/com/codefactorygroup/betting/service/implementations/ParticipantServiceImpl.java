package com.codefactorygroup.betting.service.implementations;

import com.codefactorygroup.betting.converter.ParticipantDtoToParticipantConverter;
import com.codefactorygroup.betting.domain.Participant;
import com.codefactorygroup.betting.dto.ParticipantDTO;
import com.codefactorygroup.betting.dto.RandomParticipantsDTO;
import com.codefactorygroup.betting.exception.NoSuchEntityExistsException;
import com.codefactorygroup.betting.exception.EntityAlreadyExistsException;
import com.codefactorygroup.betting.repository.ParticipantRepository;
import com.codefactorygroup.betting.service.ParticipantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service(value = "participantService")
@RequiredArgsConstructor
public class ParticipantServiceImpl implements ParticipantService {
    private final ParticipantDtoToParticipantConverter participantDtoToParticipantConverter;
    private final ParticipantRepository participantRepository;

    @Transactional
    public ParticipantDTO getParticipant(final Integer participantId) {
        return participantRepository.findById(participantId)
                .map(ParticipantDTO::converter)
                .orElseThrow(() -> new NoSuchEntityExistsException(String.format("No participant with ID=%d was found.", participantId)));

    }

    @Transactional
    public List<ParticipantDTO> getAllParticipants() {
        return participantRepository.findAll().stream().map(ParticipantDTO::converter).toList();
    }

    @Transactional
    public ParticipantDTO addParticipant(final ParticipantDTO newParticipant) {
        boolean foundParticipant = participantRepository.existsById(newParticipant.id());
        if (foundParticipant) {
            throw new EntityAlreadyExistsException(String.format("Participant with ID=%d already exists.", newParticipant.id()));
        }
        return ParticipantDTO.converter(participantRepository.
                save(participantDtoToParticipantConverter.convert(newParticipant)));
    }

    @Transactional
    public void deleteParticipant(final Integer participantId) {
        participantRepository.deleteById(participantId);
    }

    private Participant update(final Participant participant, final ParticipantDTO toUpdateParticipant) {
        participant.setId(toUpdateParticipant.id());
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
                .orElseThrow(() -> new NoSuchEntityExistsException(String.format("No participant with ID=%d was found.", participantId)));
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

}
