package com.codefactorygroup.betting.service;

import com.codefactorygroup.betting.dto.SportDTO;

public interface SportService {
    SportDTO getSport(Integer sportId);

    SportDTO addSport(SportDTO sport);

    void deleteSport(Integer sportId);

    SportDTO updateSport(SportDTO newSport, Integer sportId);
}
