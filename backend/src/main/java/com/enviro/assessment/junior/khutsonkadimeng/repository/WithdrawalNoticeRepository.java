package com.enviro.assessment.junior.khutsonkadimeng.repository;

import com.enviro.assessment.junior.khutsonkadimeng.model.WithdrawalNotice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WithdrawalNoticeRepository extends JpaRepository<WithdrawalNotice, Integer> {
    List<WithdrawalNotice> findByProductId(Integer productId);
}

