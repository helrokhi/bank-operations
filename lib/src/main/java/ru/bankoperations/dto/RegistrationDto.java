package ru.bankoperations.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationDto {
    private String login;
    private String password1;
    private String password2;
    private String balance;
    private String phoneNumber;
    private String emailAddress;
}
