package ru.bankoperations.service;

import jooq.db.tables.records.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bankoperations.dto.*;
import ru.bankoperations.repository.*;
import ru.bankoperations.repository.mapper.*;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PhoneService {
    private final PhoneRepository phoneRepository;
    private final PhoneDtoPhoneRecordMapping mapper;

    public Optional<PhoneRecord> createPhone(Long personId, RegDtoDb regDtoDb) {
        PhoneDto phoneDto = setNewPhoneDto(personId, regDtoDb.getPhoneNumber());
        PhoneRecord phoneRecord = mapper.phoneDtoToRecord(phoneDto);
        return phoneRepository.insertPhone(phoneRecord);
    }

    public void deletePhone(Long phoneId) {
        Long personId = phoneRepository.getPhoneDtoById(phoneId).getPersonId();
        int countPhoneNumber = phoneRepository.findAllPhoneNumbersByPersonId(personId).size();
        if (countPhoneNumber > 1) {
            int result = phoneRepository.deletePhoneNumber(phoneId);
        }
    }

    public int getPhoneByPhoneNumber(String phoneNumber) {
        return phoneRepository.getPhoneByPhoneNumber(phoneNumber);
    }

    private PhoneDto setNewPhoneDto(Long personId, String phoneNumber) {
        return PhoneDto.builder()
                .personId(personId)
                .phoneNumber(phoneNumber)
                .build();
    }
}
