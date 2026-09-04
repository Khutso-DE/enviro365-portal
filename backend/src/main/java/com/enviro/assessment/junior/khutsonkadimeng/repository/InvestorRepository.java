package com.enviro.assessment.junior.khutsonkadimeng.repository;

import com.enviro.assessment.junior.khutsonkadimeng.model.Investor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvestorRepository extends JpaRepository<Investor, Integer> {

}