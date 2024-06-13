package ru.bankoperations.repository.mapper;

import jooq.db.tables.records.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.bankoperations.dto.*;

@Mapper(componentModel = "spring")
public interface PhoneDtoPhoneRecordMapping {
    PhoneDto phoneRecordToDto(PhoneRecord phoneRecord);

    @Mapping(target = "id", ignore = true)
    PhoneRecord phoneDtoToRecord(PhoneDto phoneDto);
}
