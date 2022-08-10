package com.codefactorygroup.betting.converter;

import com.codefactorygroup.betting.domain.Selection;
import com.codefactorygroup.betting.dto.SelectionDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class SelectionDTOtoSelectionConverter implements Converter<SelectionDTO, Selection> {
    @Override
    public Selection convert(SelectionDTO source) {
        return Selection.builder()
                .id(source.id())
                .name(source.name())
                .odds(source.odds())
                .build();
    }
}
