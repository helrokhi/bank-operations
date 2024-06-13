package ru.bankoperations.repository;

import jooq.db.tables.records.PersonRecord;

import java.util.List;
import java.util.Optional;

public interface PersonIteraction {

    Optional<PersonRecord> insertPerson(PersonRecord personRecord);

    Optional<PersonRecord> updatePerson(PersonRecord record, String login);

    Long findUserIdByLogin(String login);

    Long getPersonIdByLogin(String login);

    Optional<PersonRecord> getPersonById(Long id);

    List<Long> findAllPersonId();
}
