package com.codefactorygroup.betting.converter;

import com.codefactorygroup.betting.domain.Participant;
import com.codefactorygroup.betting.dto.ParticipantDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ParticipantDtoToParticipantConverter implements Converter<ParticipantDTO, Participant> {
    @Override
    public Participant convert(ParticipantDTO source) {
        return Participant
                .builder()
                .name(source.name())
                .build();
    }
}
