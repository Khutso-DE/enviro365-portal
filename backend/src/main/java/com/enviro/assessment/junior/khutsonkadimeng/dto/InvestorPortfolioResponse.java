package com.enviro.assessment.junior.khutsonkadimeng.dto;

import java.util.List;

public record InvestorPortfolioResponse(
        Integer id,
        String firstName,
        String lastName,
        int age,
        List<ProductResponse> products) {
}