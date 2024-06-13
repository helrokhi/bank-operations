package ru.bankoperations.repository;

import jooq.db.tables.records.*;
import ru.bankoperations.dto.*;

import java.util.List;
import java.util.Optional;

public interface EmailRepository {
    List<EmailDto> findAllEmailAddressesByPersonId(Long personId);

    int addNewEmailAddress(EmailRecord emailRecord);

    int deleteEmailAddress(Long emailId);

    EmailDto getEmailDtoById(Long emailId);

    Optional<EmailRecord> insertEmail(EmailRecord emailRecord);

    int getEmailByEmailAddress(String emailAddress);
}
