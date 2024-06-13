package ru.bankoperations.service;

import jooq.db.tables.records.PersonRecord;
import jooq.db.tables.records.UserAuthRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.bankoperations.dto.*;
import ru.bankoperations.repository.*;
import ru.bankoperations.repository.mapper.*;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserAuthServiceImpl implements UserAuthService {
    private final UserAuthRepository userAuthRepository;
    private final PersonRepository personRepository;
    private final PersonDtoPersonRecordMapping personMapper;
    private final RegDtoDbToUserAuthRecordMapping regDtoDbToUserAuthRecordMapper;

    @Override
    public Optional<UserAuthRecord> createUserAuth(RegDtoDb regDtoDb) {
        UserAuthRecord userAuthRecord = regDtoDbToUserAuthRecordMapper.regDtoDbToUserAuthRecord(regDtoDb);
        return userAuthRepository.insertUserAuth(userAuthRecord);
    }

    @Override
    public Optional<UserAuthDto> getUserByLogin(String login) {
        return userAuthRepository.getUserByLogin(login);
    }

    @Override
    public int getUsersCountByLogin(String login) {
        return userAuthRepository.getUsersCountByLogin(login);
    }

    @Override
    public Optional<PersonDto> getAccountInfo(String login) {
        return userAuthRepository.getAccountInfo(login);
    }

    @Override
    public Optional<PersonDto> updateAccount(PersonDto personDto, String login) {
        PersonRecord record = personMapper.personDtoToPersonRecord(personDto);

        Optional<PersonRecord> resultRecord = personRepository.updatePerson(record, login);
        log.info("Update person with login: {}", login);
        return resultRecord.map(personMapper::personRecordToPersonDto);
    }

    @Override
    public Optional<PersonDto> getAccountById(Long id) {
        Optional<PersonRecord> resultRecord = personRepository.getPersonById(id);
        log.info("get person by id: {}", id);
        return resultRecord.map(personMapper::personRecordToPersonDto);
    }

    @Override
    public void deleteUserAuthById(Long id) {
        userAuthRepository.deleteUserAuthById(id);
    }
}
