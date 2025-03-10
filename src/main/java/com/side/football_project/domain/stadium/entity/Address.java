package com.side.football_project.domain.stadium.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Embeddable
@NoArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String city;
    private String state;
    private String postalCode;
    private String specificAddress;

    public Address(String city, String state, String postalCode, String specificAddress) {
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.specificAddress = specificAddress;
    }
}