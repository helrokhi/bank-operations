package ru.bankoperations.service;

import jooq.db.tables.records.PersonRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.bankoperations.dto.*;
import ru.bankoperations.repository.*;
import ru.bankoperations.repository.mapper.*;

import java.time.OffsetDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RegistrationService {

    private final PersonRepository personRepository;
    private final RegDtoDbToPersonRecordMapping mapper;

    public Optional<PersonRecord> createPerson(RegDtoDb regDtoDb, Long userId) {
        PersonRecord personRecord = mapper.regDtoDbToPersonRecord(regDtoDb);
        fillPersonDefaultValues(personRecord, userId);
        return personRepository.insertPerson(personRecord);
    }

    public void deletePersonById(Long id) {
        personRepository.deletePerson(id);
    }

    private void fillPersonDefaultValues(PersonRecord personRecord, Long userId){
        personRecord.setUserId(userId);
        personRecord.setFirstName(null);
        personRecord.setLastName(null);
        personRecord.setSurName(null);
        personRecord.setRegDate(OffsetDateTime.now());
        personRecord.setBirthDate(null);
    }
}
