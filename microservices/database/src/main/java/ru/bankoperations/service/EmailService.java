package ru.bankoperations.service;

import jooq.db.tables.records.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.bankoperations.dto.*;
import ru.bankoperations.repository.*;
import ru.bankoperations.repository.mapper.*;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    private final EmailRepository emailRepository;
    private final EmailDtoEmailRecordMapping mapper;

    public Optional<EmailRecord> createEmail(Long personId, RegDtoDb regDtoDb) {
        EmailDto emailDto = setNewEmailDto(personId, regDtoDb.getEmailAddress());
        EmailRecord emailRecord = mapper.emailDtoToRecord(emailDto);
        return emailRepository.insertEmail(emailRecord);
    }

    public void deleteEmailAddress(Long emailId) {
        Long personId = emailRepository.getEmailDtoById(emailId).getPersonId();
        int countPhoneNumber = emailRepository.findAllEmailAddressesByPersonId(personId).size();
        if (countPhoneNumber > 1) {
            int result = emailRepository.deleteEmailAddress(emailId);
        }
    }

    private EmailDto setNewEmailDto(Long personId, String emailAddress) {
        return EmailDto.builder()
                .personId(personId)
                .emailAddress(emailAddress)
                .build();
    }

    public int getEmailByEmailAddress(String emailAddress) {
        return emailRepository.getEmailByEmailAddress(emailAddress);
    }
}
