package com.enviro.assessment.junior.khutsonkadimeng.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "withdrawal_notices")
public class WithdrawalNotice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Double withdrawalAmount;
    private LocalDateTime noticeDate;
    private String status;
    private String bankingDetails;
    private Integer productId; // foreign key
}