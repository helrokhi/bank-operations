package ru.bankoperations.service;

import jooq.db.tables.records.PersonRecord;
import jooq.db.tables.records.TransferRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.bankoperations.dto.*;
import ru.bankoperations.dto.enums.TransferType;
import ru.bankoperations.repository.*;
import ru.bankoperations.repository.mapper.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransferService {
    private final TransferRepository transferRepository;
    private final PersonRepository personRepository;
    private final TransferDtoTransferRecordMapping transferMapper;
    private final PersonDtoPersonRecordMapping personMapper;

    public PersonDto getPersonDtoById(Long id, String login) {
        Long personId = personRepository.getPersonIdByLogin(login);

        return !personId.equals(id) ?
                transferRepository.getPersonDtoById(id) :
                null;
    }

    public boolean isTransfer(String login, Long toPersonId, String amount) {
        Long fromPersonId = personRepository.getPersonIdByLogin(login);
        boolean result;
        PersonDto fromPersonDto = transferRepository.getPersonDtoById(fromPersonId);
        PersonDto toPersonDto = transferRepository.getPersonDtoById(toPersonId);

        log.info("transfer from {} to {} amount - {}", fromPersonDto, toPersonDto, amount);

        if (fromPersonId > toPersonId) {
            synchronized (fromPersonDto) {
                synchronized (toPersonDto) {
                    result = transaction(fromPersonDto, toPersonDto, amount);
                }
            }
        } else {
            synchronized (toPersonDto) {
                synchronized (fromPersonDto) {
                    result = transaction(fromPersonDto, toPersonDto, amount);
                }
            }
        }
        return result;
    }

    private boolean isEnough(PersonDto personDto, String amount) {
        //Достаточно средств
        return new BigDecimal(personDto.getBalance()).compareTo(new BigDecimal(amount)) >= 0;
    }

    private boolean isEquals(PersonDto fromPersonDto, PersonDto toPersonDto) {
        return fromPersonDto.equals(toPersonDto);
    }

    private boolean isPositive(String amount) {
        return new BigDecimal(amount).compareTo(BigDecimal.ZERO) >= 0;
    }

    private boolean isPossible(PersonDto fromPersonDto, PersonDto toPersonDto, String amount) {
        return !isEquals(fromPersonDto, toPersonDto) &
                isPositive(amount) &
                isEnough(fromPersonDto, amount);
    }

    private boolean transaction(PersonDto fromPersonDto, PersonDto toPersonDto, String amount) {
        log.info("transaction from {} to {} amount - {}", fromPersonDto, toPersonDto, amount);
        boolean result = false;
        if (isPossible(fromPersonDto, toPersonDto, amount)) {
            TransferRecord fromTransferRecord = isTransfer(
                    fromPersonDto, toPersonDto, amount, TransferType.TAKE_MONEY_TRANSFER);

            TransferRecord toTransferRecord = isTransfer(
                    fromPersonDto, toPersonDto, amount, TransferType.PUT_MONEY_TRANSFER);

            PersonRecord fromPersonRecord = take(fromPersonDto, amount);
            PersonRecord toPersonRecord = put(toPersonDto, amount);

            result = transferRepository.insertTransfersAndUpdatePersonAfterTransfer(
                    fromPersonRecord, toPersonRecord,
                    fromTransferRecord, toTransferRecord);

            log.info("transaction from {} to {} amount - {} is done", fromPersonDto, toPersonDto, amount);


        } else {
            log.info("transaction from {} to {} amount - {} not possible", fromPersonDto, toPersonDto, amount);
        }
        return result;
    }

    private PersonRecord take(PersonDto personDto, String amount) {
        BigDecimal oldBalance = new BigDecimal(personDto.getBalance());
        BigDecimal transferAmount = new BigDecimal(amount);
        BigDecimal newBalance = oldBalance.subtract(transferAmount);
        personDto.setBalance(newBalance.toEngineeringString());
        return personMapper.personDtoToPersonRecord(personDto);
    }

    private PersonRecord put(PersonDto personDto, String amount) {
        BigDecimal oldBalance = new BigDecimal(personDto.getBalance());
        BigDecimal transferAmount = new BigDecimal(amount);
        BigDecimal newBalance = oldBalance.add(transferAmount);
        personDto.setBalance(newBalance.toEngineeringString());
        return personMapper.personDtoToPersonRecord(personDto);
    }

    private TransferRecord isTransfer(PersonDto fromPersonDto, PersonDto toPersonDto,
                                      String amount, TransferType type) {
        BigDecimal oldBalanceSender = new BigDecimal(fromPersonDto.getBalance());
        BigDecimal oldBalanceBeneficiary = new BigDecimal(toPersonDto.getBalance());

        Long senderPersonId = fromPersonDto.getId();
        Long beneficiaryPersonId = toPersonDto.getId();

        BigDecimal transferAmount = new BigDecimal(amount);

        BigDecimal newBalanceSender = oldBalanceSender;
        BigDecimal newBalanceBeneficiary = oldBalanceBeneficiary;

        boolean isDone = false;

        if (type.equals(TransferType.TAKE_MONEY_TRANSFER)) {
            newBalanceSender = oldBalanceSender.subtract(transferAmount);
            newBalanceBeneficiary = oldBalanceBeneficiary.add(transferAmount);
            isDone = true;
        }

        if (type.equals(TransferType.PUT_MONEY_TRANSFER)) {
            newBalanceSender = oldBalanceSender.add(transferAmount);
            newBalanceBeneficiary = oldBalanceBeneficiary.subtract(transferAmount);
            isDone = true;
        }

        TransferDto transferDto = TransferDto.builder()
                .transferTypeId(type.getId())
                .transferDate(OffsetDateTime.now())
                .senderId(senderPersonId)
                .beneficiaryId(beneficiaryPersonId)
                .transferAmount(transferAmount.toEngineeringString())
                .isDone(isDone)
                .oldBalanceSender(oldBalanceSender.toEngineeringString())
                .newBalanceSender(newBalanceSender.toEngineeringString())
                .oldBalanceBeneficiary(oldBalanceBeneficiary.toEngineeringString())
                .newBalanceBeneficiary(newBalanceBeneficiary.toEngineeringString())
                .build();

        return transferMapper.transferDtoToRecord(transferDto);
    }
}
