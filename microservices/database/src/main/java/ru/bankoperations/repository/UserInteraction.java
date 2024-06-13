package ru.bankoperations.repository;

import jooq.db.tables.records.PersonRecord;

import java.util.Optional;

public interface UserInteraction {

    Optional<PersonRecord> insertPerson(PersonRecord personRecord);

    Optional<PersonRecord> updatePerson(PersonRecord record, String login);

    Long findUserIdByLogin(String login);

    Optional<PersonRecord> getPersonById(Long id);
}
