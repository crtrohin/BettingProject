package com.codefactorygroup.betting.converter;

import com.codefactorygroup.betting.domain.Participant;
import com.codefactorygroup.betting.dto.ParticipantDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ParticipantToParticipantDtoConverter implements Converter<Participant, ParticipantDTO> {

    @Override
    public ParticipantDTO convert(Participant source) {
      return ParticipantDTO.builder()
                .dtoID(source.getId())
                .name(source.getName())
                .build();
    }
}
