package ru.bankoperations.repository;

import jooq.db.Tables;
import jooq.db.tables.records.*;
import lombok.AllArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.exception.DataAccessException;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import ru.bankoperations.dto.*;
import ru.bankoperations.repository.mapper.*;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class DepositRepositoryImpl implements DepositRepository{
    private final DSLContext dsl;
    private final DataSourceTransactionManager transactionManager;
    private final DepositDtoDepositRecordMapping depositMapper;
    private final PersonDtoPersonRecordMapping personMapper;

    @Override
    public PersonDto getPersonDtoById(Long personId) {
        return dsl
                .selectFrom(Tables.PERSON)
                .where(Tables.PERSON.ID.eq(personId))
                .fetchOptional()
                .map(personMapper::personRecordToPersonDto)
                .orElse(null);
    }

    @Override
    public DepositDto getDepositDtoByPersonId(Long personId) {
        return dsl
                .selectFrom(Tables.DEPOSIT)
                .where(Tables.DEPOSIT.PERSON_ID.eq(personId))
                .fetchOptional()
                .map(depositMapper::depositRecordToDto)
                .orElse(null);
    }

    @Override
    public boolean updateDepositPercentAndBalance(
            PersonRecord personRecord,
            DepositRecord depositRecord,
            TransferRecord transferRecord) {
        boolean isDone = false;
        TransactionStatus transactionStatus = transactionManager
                .getTransaction(new DefaultTransactionDefinition());
        try {
            dsl
                    .update(Tables.PERSON)
                    .set(dsl.newRecord(Tables.PERSON, personRecord))
                    .where(Tables.PERSON.ID.eq(personRecord.getId()))
                    .execute();

            dsl
                    .update(Tables.DEPOSIT)
                    .set(dsl.newRecord(Tables.DEPOSIT, depositRecord))
                    .where(Tables.DEPOSIT.PERSON_ID.eq(depositRecord.getPersonId()))
                    .execute();

            dsl
                    .insertInto(Tables.TRANSFER)
                    .set(transferRecord)
                    .execute();

            transactionManager.commit(transactionStatus);
            isDone = true;

        } catch (DataAccessException exception) {
            transactionManager.rollback(transactionStatus);
        }
        return isDone;
    }

    @Override
    public Optional<DepositRecord> insertDeposit(DepositRecord depositRecord) {
        return dsl
                .insertInto(Tables.DEPOSIT)
                .set(depositRecord)
                .returning()
                .fetchOptional();
    }

    @Override
    public int setIsDone(Long personId) {
        return dsl
                .update(Tables.DEPOSIT)
                .set(Tables.DEPOSIT.IS_DONE, true)
                .where(Tables.DEPOSIT.PERSON_ID.eq(personId))
                .returning()
                .execute();
    }

    @Override
    public List<Long> findAllPersonId() {
        return dsl
                .select(Tables.DEPOSIT.PERSON_ID)
                .from(Tables.DEPOSIT)
                .where(Tables.DEPOSIT.IS_DONE.eq(false))
                .fetch()
                .map(Record1::component1);
    }
}
