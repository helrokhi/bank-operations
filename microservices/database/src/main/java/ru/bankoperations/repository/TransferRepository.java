package ru.bankoperations.repository;

import jooq.db.tables.records.*;
import ru.bankoperations.dto.*;

import java.util.Optional;

public interface TransferRepository {
    PersonDto getPersonDtoById(Long personId);

    boolean insertTransfersAndUpdatePersonAfterTransfer(
            PersonRecord fromPersonRecord,
            PersonRecord toPersonRecord,
            TransferRecord tromTransferRecord,
            TransferRecord toTransferRecord);
}
