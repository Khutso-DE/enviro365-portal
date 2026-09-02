package com.enviro.assessment.junior.khutsonkadimeng.model;

import jakarta.persistence.*;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String type; // "RETIREMENT", "SAVINGS"
    private String name;
    private Double currentBalance;
    private Integer investorId; // foreign key
}