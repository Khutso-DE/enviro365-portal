package com.enviro.assessment.junior.khutsonkadimeng.dto;

public record ProductResponse(
        Integer id,
        String type,
        String name,
        Double currentBalance) {
}