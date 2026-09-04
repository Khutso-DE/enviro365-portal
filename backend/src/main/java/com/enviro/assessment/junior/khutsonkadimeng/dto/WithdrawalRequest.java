package com.enviro.assessment.junior.khutsonkadimeng.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record WithdrawalRequest(
    // validate all the required fields for withdrawal
        @NotNull(message = "Product ID is required")
        Integer productId,

        @NotNull(message = "Withdrawal amount is required") @Positive(message = "Withdrawal amount must be greater than zero")
        Double withdrawalAmount,

        @NotBlank(message = "Banking details cannot be empty")
        String bankingDetails) {
}