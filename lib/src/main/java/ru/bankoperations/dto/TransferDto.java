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
public class TransferDto {
    private Long id;
    private int transferTypeId;
    private OffsetDateTime transferDate;
    private Long senderId;
    private Long beneficiaryId;
    private String transferAmount;
    private Boolean isDone;
    private String oldBalanceSender;
    private String newBalanceSender;
    private String oldBalanceBeneficiary;
    private String newBalanceBeneficiary;
}
