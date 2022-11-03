package com.codefactorygroup.betting.converter;

import com.codefactorygroup.betting.domain.SelectionResult;
import com.codefactorygroup.betting.dto.SelectionResultDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class SelectionResultDTOtoSelectionResultConverter implements Converter<SelectionResultDTO, SelectionResult> {
    @Override
    public SelectionResult convert(SelectionResultDTO source) {
        return Enum.valueOf(SelectionResult.class, source.result());
    }
}

