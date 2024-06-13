package ru.bankoperations.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegDtoDb {
    private String login;
    private String password;
    private String balance;
    private String phoneNumber;
    private String emailAddress;
}
