package ru.bankoperations.repository;

import jooq.db.tables.records.*;
import ru.bankoperations.dto.*;

import java.util.List;
import java.util.Optional;

public interface PhoneRepository {
    List<PhoneDto> findAllPhoneNumbersByPersonId(Long personId);

    int addNewPhoneNumber(PhoneRecord phoneRecord);

    int deletePhoneNumber(Long phoneId);

    PhoneDto getPhoneDtoById(Long phoneId);

    Optional<PhoneRecord> insertPhone(PhoneRecord phoneRecord);

    int getPhoneByPhoneNumber(String phoneNumber);
}
