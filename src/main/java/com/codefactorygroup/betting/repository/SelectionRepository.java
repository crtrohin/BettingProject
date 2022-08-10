package com.codefactorygroup.betting.repository;

import com.codefactorygroup.betting.domain.Selection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SelectionRepository extends JpaRepository<Selection, Integer> {
}
