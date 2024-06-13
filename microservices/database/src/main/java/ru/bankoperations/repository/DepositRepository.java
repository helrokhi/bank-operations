package ru.bankoperations.repository;

import jooq.db.tables.records.*;
import ru.bankoperations.dto.*;

import java.util.List;
import java.util.Optional;

public interface DepositRepository {
    PersonDto getPersonDtoById(Long personId);

    DepositDto getDepositDtoByPersonId(Long personId);

    boolean updateDepositPercentAndBalance(
            PersonRecord personRecord,
            DepositRecord depositRecord,
            TransferRecord transferRecord);

    Optional<DepositRecord> insertDeposit(DepositRecord depositRecord);

    int setIsDone(Long personId);

    List<Long> findAllPersonId();
}
