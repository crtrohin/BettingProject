package com.codefactorygroup.betting.converter;

import com.codefactorygroup.betting.domain.Market;
import com.codefactorygroup.betting.dto.MarketDTO;
import com.codefactorygroup.betting.dto.SelectionDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class MarketDTOtoMarketConverter implements Converter<MarketDTO, Market> {

    private final SelectionDTOtoSelectionConverter selectionDTOtoSelectionConverter;

    public MarketDTOtoMarketConverter(SelectionDTOtoSelectionConverter selectionDTOtoSelectionConverter) {
        this.selectionDTOtoSelectionConverter = selectionDTOtoSelectionConverter;
    }


    @Override
    public Market convert(MarketDTO source) {
        List<SelectionDTO> selectionList = Optional.of(source).map(MarketDTO::selections).orElseGet(Collections::emptyList);
        return Market.builder()
                .id(source.id())
                .name(source.name())
                .selections(selectionList
                        .stream()
                        .map(selectionDTOtoSelectionConverter::convert)
                        .toList())
                .build();
    }
}
