package ru.bankoperations.service;

import jooq.db.tables.records.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.bankoperations.dto.*;
import ru.bankoperations.dto.enums.TransferType;
import ru.bankoperations.repository.*;
import ru.bankoperations.repository.mapper.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DepositService {
    private final DepositRepository depositRepository;
    private final DepositDtoDepositRecordMapping depositMapper;
    private final TransferDtoTransferRecordMapping transferMapper;
    private final PersonDtoPersonRecordMapping personMapper;

    private final BigDecimal LIMIT_REGEX = BigDecimal.valueOf(2);
    private final BigDecimal PERCENT_REGEX = BigDecimal.valueOf(0.05);

    public Optional<DepositRecord> createDeposit(Long personId, RegDtoDb regDtoDb) {
        DepositDto depositDto = setNewDepositDto(personId, regDtoDb.getBalance());
        DepositRecord depositRecord = depositMapper.depositDtoToRecord(depositDto);
        return depositRepository.insertDeposit(depositRecord);
    }

    public boolean updateDeposit(Long personId) {
        PersonDto personDto = depositRepository.getPersonDtoById(personId);

        return updateDepositPercentAndBalance(personDto);
    }

    public List<Long> findAllPersonId() {
        return depositRepository.findAllPersonId();
    }

    private DepositDto setNewDepositDto(Long personId, String balance) {
        BigDecimal depositLimit = new BigDecimal(balance).multiply(LIMIT_REGEX);

        return DepositDto.builder()
                .personId(personId)
                .initialDeposit(balance)
                .depositLimit(depositLimit.toEngineeringString())
                .depositPercent("0.00")
                .isDone(false)
                .build();
    }

    private synchronized boolean updateDepositPercentAndBalance(PersonDto personDto) {
        Long personId = personDto.getId();
        boolean result = false;
        String balance = personDto.getBalance();

        DepositDto depositDto = depositRepository.getDepositDtoByPersonId(personId);

        BigDecimal percent = new BigDecimal(personDto.getBalance()).multiply(PERCENT_REGEX)
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal oldDepositPercent = new BigDecimal(depositDto.getDepositPercent());
        BigDecimal newDepositPercent = oldDepositPercent.add(percent);

        BigDecimal depositLimit = new BigDecimal(depositDto.getDepositLimit());

        log.info("update balance personDto {}, isPossible - {}, percent - {}, balance - {}",
                personDto.getId(),
                isPossibleDeposit(newDepositPercent, depositLimit),
                percent,
                balance);

        log.info("update depositPercent and balance personDto {}, isPossible - {}, percent - {}",
                personDto.getId(),
                isPossibleDeposit(newDepositPercent, depositLimit),
                percent);

        boolean isDone = depositDto.getIsDone();

        if (isPossibleDeposit(newDepositPercent, depositLimit) &
                isPositiveBalance(balance) &
                !isDone) {
            result = isDoneUpdateDeposit(personDto, depositDto, percent);
        }

        if (!isPossibleDeposit(newDepositPercent, depositLimit) & !isDone) {
            percent = depositLimit.subtract(oldDepositPercent);
            result = isDoneUpdateDeposit(personDto, depositDto, percent);
            depositRepository.setIsDone(personId);
        }

        return result;
    }

    private PersonRecord updateBalance(PersonDto personDto, BigDecimal percent) {
        BigDecimal oldBalance = new BigDecimal(personDto.getBalance());
        BigDecimal newBalance = oldBalance.add(percent);

        personDto.setBalance(newBalance.toEngineeringString());
        log.info("update balance personDto - {}, percent - {}, oldBalance - {}, newBalance - {}",
                personDto.getId(), percent, oldBalance, newBalance);
        return personMapper.personDtoToPersonRecord(personDto);
    }

    private DepositRecord updateDepositPercent(DepositDto depositDto, BigDecimal percent) {
        BigDecimal oldDepositPercent = new BigDecimal(depositDto.getDepositPercent());
        BigDecimal newDepositPercent = oldDepositPercent.add(percent);

        depositDto.setDepositPercent(newDepositPercent.toEngineeringString());
        log.info("update percent depositDto - {}, percent - {}, old - {}, new - {}",
                depositDto.getPersonId(), percent, oldDepositPercent, newDepositPercent);
        return depositMapper.depositDtoToRecord(depositDto);
    }

    private boolean isPossibleDeposit(BigDecimal depositPercent, BigDecimal depositLimit) {
        return depositLimit.compareTo(depositPercent) > 0;
    }

    private boolean isPositiveBalance(String balance) {
        return new BigDecimal(balance).compareTo(BigDecimal.ZERO) >= 0;
    }

    private boolean isDoneUpdateDeposit(PersonDto personDto, DepositDto depositDto, BigDecimal percent) {
        TransferRecord transferRecord = setTransferRecord(
                personDto, percent, TransferType.DEPOSIT_PERCENT);

        PersonRecord personRecord = updateBalance(personDto, percent);
        DepositRecord depositRecord = updateDepositPercent(depositDto, percent);


        return depositRepository.updateDepositPercentAndBalance(
                personRecord,
                depositRecord,
                transferRecord);
    }

    private TransferRecord setTransferRecord(PersonDto personDto,
                                             BigDecimal percent, TransferType type) {
        BigDecimal oldBalanceSender = new BigDecimal(personDto.getBalance());

        Long senderPersonId = personDto.getId();

        BigDecimal newBalanceSender = oldBalanceSender;

        boolean isDone = false;

        if (type.equals(TransferType.DEPOSIT_PERCENT)) {
            newBalanceSender = oldBalanceSender.add(percent);
            isDone = true;
        }

        TransferDto transferDto = TransferDto.builder()
                .transferTypeId(type.getId())
                .transferDate(OffsetDateTime.now())
                .senderId(senderPersonId)
                .beneficiaryId(null)
                .transferAmount(percent.toEngineeringString())
                .isDone(isDone)
                .oldBalanceSender(oldBalanceSender.toEngineeringString())
                .newBalanceSender(newBalanceSender.toEngineeringString())
                .oldBalanceBeneficiary(null)
                .newBalanceBeneficiary(null)
                .build();

        log.info("set transferDto - {}, percent - {}, old - {}, new - {}",
                transferDto.getSenderId(), percent, oldBalanceSender, newBalanceSender);

        return transferMapper.transferDtoToRecord(transferDto);
    }
}
