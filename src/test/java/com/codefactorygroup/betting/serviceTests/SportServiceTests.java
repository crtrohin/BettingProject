package com.codefactorygroup.betting.serviceTests;

import com.codefactorygroup.betting.converter.SportDTOtoSportConverter;
import com.codefactorygroup.betting.domain.Sport;
import com.codefactorygroup.betting.dto.SportDTO;
import com.codefactorygroup.betting.exception.EntityAlreadyExistsException;
import com.codefactorygroup.betting.exception.NoSuchEntityExistsException;
import com.codefactorygroup.betting.repository.SportRepository;
import com.codefactorygroup.betting.service.implementations.SportServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SportServiceTests {

    @Mock
    private SportDTOtoSportConverter sportDtoToSportConverter;
    @Mock
    private SportRepository sportRepository;

    @InjectMocks
    private SportServiceImpl sportService;

    @Test
    void getSportShouldReturnSport() {
        Optional<Sport> optionalSport = Optional.of(Sport
                .builder()
                .id(10)
                .name("Tennis")
                .build());

        when(sportRepository.findById(10)).thenReturn(optionalSport);

        SportDTO sportDTO = sportService.getSport(10);
        Sport sport = optionalSport.get();

        assertThat(sportDTO).isNotNull();
        assertEquals(sport.getId(), sportDTO.id());
        assertEquals(sport.getName(), sportDTO.name());
    }

    @Test
    void getSportShouldReturnException() {
        when(sportRepository.findById(10)).thenReturn(Optional.empty());

        assertThrows(NoSuchEntityExistsException.class, () -> sportService.getSport(10));
    }

    @Test
    void getAllSportsShouldReturnSports() {
        Sport sport1 = Sport
                .builder()
                .id(1)
                .name("Tennis")
                .build();

        Sport sport2 = Sport
                .builder()
                .id(2)
                .name("Football")
                .build();

        Sport sport3 = Sport
                .builder()
                .id(3)
                .name("Swimming")
                .build();

        List<Sport> sports = List.of(sport1, sport2, sport3);

        when(sportRepository.findAll()).thenReturn(sports);

        List<SportDTO> sportDTOS = sportService.getAllSports();

        assertEquals(sportDTOS.get(0).id(), sport1.getId());
        assertEquals(sportDTOS.get(0).name(), sport1.getName());

        assertEquals(sportDTOS.get(1).id(), sport2.getId());
        assertEquals(sportDTOS.get(1).name(), sport2.getName());

        assertEquals(sportDTOS.get(2).id(), sport3.getId());
        assertEquals(sportDTOS.get(2).name(), sport3.getName());
    }


    @Test
    void addSportShouldReturnSport() {
        SportDTO sportDTO = SportDTO
                .builder()
                .name("Football")
                .competitions(Collections.emptyList())
                .build();

        Sport sport1 = Sport
                .builder()
                .name("Football")
                .competitions(Collections.emptyList())
                .build();

        when(sportRepository.existsByName("Football")).thenReturn(false);
        when(sportDtoToSportConverter.convert(sportDTO)).thenReturn(sport1);
        when(sportRepository.save(sport1)).thenReturn(sport1);

        SportDTO savedSport = sportService.addSport(sportDTO);

        assertEquals(sportDTO, savedSport);
    }


    @Test
    void addSportShouldReturnExceptionSportAlreadyExists() {
        SportDTO sportDTO = SportDTO
                .builder()
                .name("Football")
                .competitions(Collections.emptyList())
                .build();

        when(sportRepository.existsByName("Football")).thenReturn(true);

        assertThrows(EntityAlreadyExistsException.class, () -> sportService.addSport(sportDTO));
    }

    @Test
    void updateSportShouldReturnSport() {
        Sport sportFromDb = Sport
                .builder()
                .id(1)
                .name("Football")
                .competitions(Collections.emptyList())
                .build();

        Sport updatedSport = Sport
                .builder()
                .id(1)
                .name("Tennis")
                .competitions(Collections.emptyList())
                .build();

        SportDTO toUpdateSportDTO = SportDTO
                .builder()
                .id(1)
                .name("Tennis")
                .competitions(Collections.emptyList())
                .build();

        when(sportRepository.findById(1)).thenReturn(Optional.of(sportFromDb));
        when(sportRepository.save(updatedSport)).thenReturn(updatedSport);

        SportDTO resultedSportDTO = sportService.updateSport(toUpdateSportDTO, 1);

        assertEquals(resultedSportDTO, toUpdateSportDTO);
    }

    @Test
    void updateSportShouldReturnException() {
        SportDTO toUpdateSportDTO = SportDTO
                .builder()
                .id(1)
                .name("Tennis")
                .build();

        when(sportRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(NoSuchEntityExistsException.class, () -> sportService.updateSport(toUpdateSportDTO, 1));
    }


    @Test
    void deleteSport() {
        when(sportRepository.existsById(1)).thenReturn(true);

        sportService.deleteSport(1);

        // check if the method was called
        verify(sportRepository).deleteById(1);
    }


    @Test
    void deleteSportShouldReturnException() {
        when(sportRepository.existsById(1)).thenReturn(false);

        assertThrows(NoSuchEntityExistsException.class, () -> sportService.deleteSport(1));
    }

}
