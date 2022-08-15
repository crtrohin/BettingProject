package com.codefactorygroup.betting.repository;

import com.codefactorygroup.betting.domain.Sport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SportRepository extends JpaRepository<Sport, Integer> {

}
