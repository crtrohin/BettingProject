package com.codefactorygroup.betting.serviceTests;

import com.codefactorygroup.betting.converter.SelectionDTOtoSelectionConverter;
import com.codefactorygroup.betting.domain.Market;
import com.codefactorygroup.betting.domain.Selection;
import com.codefactorygroup.betting.dto.SelectionDTO;
import com.codefactorygroup.betting.exception.EntityAlreadyExistsException;
import com.codefactorygroup.betting.exception.NoSuchEntityExistsException;
import com.codefactorygroup.betting.repository.SelectionRepository;
import com.codefactorygroup.betting.repository.MarketRepository;
import com.codefactorygroup.betting.service.implementations.SelectionServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SelectionServiceTests {

    @Mock
    private SelectionDTOtoSelectionConverter selectionDtoToSelectionConverter;
    @Mock
    private SelectionRepository selectionRepository;

    @Mock
    private MarketRepository marketRepository;

    @InjectMocks
    private SelectionServiceImpl selectionService;

    @Test
    void getSelectionShouldReturnSelection() {
        Optional<Selection> optionalSelection = Optional.of(Selection
                .builder()
                .id(10)
                .name("Senegal")
                .build());

        when(selectionRepository.findById(10)).thenReturn(optionalSelection);

        SelectionDTO selectionDTO = selectionService.getSelection(10);
        Selection selection = optionalSelection.get();

        assertThat(selectionDTO).isNotNull();
        assertEquals(selection.getId(), selectionDTO.id());
        assertEquals(selection.getName(), selectionDTO.name());
    }

    @Test
    void getSelectionShouldReturnException() {
        when(selectionRepository.findById(10)).thenReturn(Optional.empty());

        assertThrows(NoSuchEntityExistsException.class, () -> selectionService.getSelection(10));
    }

    @Test
    void getAllSelectionsShouldReturnSelections() {
        Selection selection1 = Selection
                .builder()
                .id(1)
                .name("Senegal")
                .build();

        Selection selection2 = Selection
                .builder()
                .id(2)
                .name("Draw")
                .build();

        Selection selection3 = Selection
                .builder()
                .id(3)
                .name("Netherlands")
                .build();

        List<Selection> selections = List.of(selection1, selection2, selection3);

        when(selectionRepository.findAll()).thenReturn(selections);

        List<SelectionDTO> selectionDTOS = selectionService.getAllSelections();

        assertEquals(selectionDTOS.get(0).id(), selection1.getId());
        assertEquals(selectionDTOS.get(0).name(), selection1.getName());

        assertEquals(selectionDTOS.get(1).id(), selection2.getId());
        assertEquals(selectionDTOS.get(1).name(), selection2.getName());

        assertEquals(selectionDTOS.get(2).id(), selection3.getId());
        assertEquals(selectionDTOS.get(2).name(), selection3.getName());
    }


    @Test
    void addSelectionShouldReturnSelection() {
        SelectionDTO selectionDTO = SelectionDTO
                .builder()
                .name("Senegal")
                .odds(10)
                .build();

        Selection selection1 = Selection
                .builder()
                .name("Senegal")
                .odds(10)
                .build();

        Selection selection2 = Selection
                .builder()
                .id(2)
                .name("Netherlands")
                .odds(3)
                .build();

        List<Selection> selections = new ArrayList<>();
        selections.add(selection2);

        Market market = Market
                .builder()
                .id(1)
                .name("1x2")
                .selections(selections)
                .build();

        when(marketRepository.findById(1)).thenReturn(Optional.of(market));
        when(selectionRepository.existsByName("Senegal")).thenReturn(false);
        when(selectionDtoToSelectionConverter.convert(selectionDTO)).thenReturn(selection1);
        when(selectionRepository.save(selection1)).thenReturn(selection1);

        SelectionDTO savedSelection = selectionService.addSelection(1, selectionDTO);

        assertEquals(selectionDTO, savedSelection);
    }

    @Test
    void addSelectionShouldReturnExceptionNoMarketExists() {
        SelectionDTO selectionDTO = SelectionDTO
                .builder()
                .name("Senegal")
                .odds(10)
                .build();

        when(marketRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(NoSuchEntityExistsException.class, () -> selectionService.addSelection(1, selectionDTO));
    }

    @Test
    void addSelectionShouldReturnExceptionSelectionAlreadyExists() {
        SelectionDTO selectionDTO = SelectionDTO
                .builder()
                .name("Senegal")
                .odds(10)
                .build();

        Selection selection1 = Selection
                .builder()
                .name("Senegal")
                .odds(10)
                .build();

        List<Selection> selections = new ArrayList<>();
        selections.add(selection1);

        Market market = Market
                .builder()
                .id(1)
                .name("1x2")
                .selections(selections)
                .build();

        when(marketRepository.findById(1)).thenReturn(Optional.of(market));
        when(selectionRepository.existsByName("Senegal")).thenReturn(true);

        assertThrows(EntityAlreadyExistsException.class, () -> selectionService.addSelection(1, selectionDTO));
    }

    @Test
    void updateSelectionShouldReturnSelection() {
        Selection selectionFromDb = Selection
                .builder()
                .id(1)
                .name("Senegal")
                .odds(10)
                .build();

        Selection updatedSelection = Selection
                .builder()
                .id(1)
                .name("Netherlands")
                .odds(10)
                .build();

        SelectionDTO toUpdateSelectionDTO = SelectionDTO
                .builder()
                .id(1)
                .name("Netherlands")
                .odds(10)
                .build();

        when(selectionRepository.findById(1)).thenReturn(Optional.of(selectionFromDb));
        when(selectionRepository.save(updatedSelection)).thenReturn(updatedSelection);

        SelectionDTO resultedSelectionDTO = selectionService.updateSelection(toUpdateSelectionDTO, 1);

        assertEquals(resultedSelectionDTO, toUpdateSelectionDTO);
    }

    @Test
    void updateSelectionShouldReturnException() {
        SelectionDTO toUpdateSelectionDTO = SelectionDTO
                .builder()
                .id(1)
                .name("Senegal")
                .build();

        when(selectionRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(NoSuchEntityExistsException.class, () -> selectionService.updateSelection(toUpdateSelectionDTO, 1));
    }


    @Test
    void deleteSelection() {
        when(selectionRepository.existsById(1)).thenReturn(true);

        selectionService.deleteSelection(1);

        // check if the method was called
        verify(selectionRepository).deleteById(1);
    }


    @Test
    void deleteSelectionShouldReturnException() {
        when(selectionRepository.existsById(1)).thenReturn(false);

        assertThrows(NoSuchEntityExistsException.class, () -> selectionService.deleteSelection(1));
    }

}
