package ru.bankoperations.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepositDto {
    private Long id;
    private Long personId;
    private String initialDeposit;
    private String depositLimit;
    private String depositPercent;
    private Boolean isDone;
}
