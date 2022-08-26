package com.codefactorygroup.betting.service;

import com.codefactorygroup.betting.dto.SportDTO;

import java.util.List;

public interface SportService {
    SportDTO getSport(Integer sportId);

    List<SportDTO> getAllSports();

    SportDTO addSport(SportDTO sport);

    void deleteSport(Integer sportId);

    SportDTO updateSport(SportDTO newSport, Integer sportId);
}
