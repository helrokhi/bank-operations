package ru.bankoperations.repository;

import jooq.db.Tables;
import jooq.db.tables.records.*;
import lombok.AllArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.exception.DataAccessException;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import ru.bankoperations.dto.*;
import ru.bankoperations.repository.mapper.*;

@Repository
@AllArgsConstructor
public class TransferRepositoryImpl implements TransferRepository{
    private final DSLContext dsl;
    private final PersonDtoPersonRecordMapping mapper;
    private final DataSourceTransactionManager transactionManager;

    public PersonDto getPersonDtoById(Long personId) {
        return dsl
                .selectFrom(Tables.PERSON)
                .where(Tables.PERSON.ID.eq(personId))
                .fetchOptional()
                .map(mapper::personRecordToPersonDto)
                .orElse(null);
    }

    @Override
    public boolean insertTransfersAndUpdatePersonAfterTransfer(
            PersonRecord fromPersonRecord,
            PersonRecord toPersonRecord,
            TransferRecord tromTransferRecord,
            TransferRecord toTransferRecord) {
        boolean isDone = false;
        TransactionStatus transactionStatus = transactionManager
                .getTransaction(new DefaultTransactionDefinition());
        try {
            dsl
                    .update(Tables.PERSON)
                    .set(dsl.newRecord(Tables.PERSON, fromPersonRecord))
                    .where(Tables.PERSON.ID.eq(fromPersonRecord.getId()))
                    .execute();

            dsl
                    .update(Tables.PERSON)
                    .set(dsl.newRecord(Tables.PERSON, toPersonRecord))
                    .where(Tables.PERSON.ID.eq(toPersonRecord.getId()))
                    .execute();

            dsl
                    .insertInto(Tables.TRANSFER)
                    .set(tromTransferRecord)
                    .execute();

            dsl
                    .insertInto(Tables.TRANSFER)
                    .set(toTransferRecord)
                    .execute();

            transactionManager.commit(transactionStatus);
            isDone = true;

        } catch (DataAccessException exception) {
            transactionManager.rollback(transactionStatus);
        }
        return isDone;
    }
}
