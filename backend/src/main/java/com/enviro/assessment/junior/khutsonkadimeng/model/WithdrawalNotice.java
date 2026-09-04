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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getWithdrawalAmount() {
        return withdrawalAmount;
    }

    public void setWithdrawalAmount(Double withdrawalAmount) {
        this.withdrawalAmount = withdrawalAmount;
    }

    public LocalDateTime getNoticeDate() {
        return noticeDate;
    }

    public void setNoticeDate(LocalDateTime noticeDate) {
        this.noticeDate = noticeDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBankingDetails() {
        return bankingDetails;
    }

    public void setBankingDetails(String bankingDetails) {
        this.bankingDetails = bankingDetails;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }
}