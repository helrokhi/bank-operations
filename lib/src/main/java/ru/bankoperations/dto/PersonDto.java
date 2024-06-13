package ru.bankoperations.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonDto {
    private Long id;
    private Long userId;
    private String firstName;
    private String lastName;
    private String surName;
    private String balance;
    private OffsetDateTime regDate;
    private OffsetDateTime birthDate;
}
