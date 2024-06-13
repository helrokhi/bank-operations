package ru.bankoperations.repository.mapper;

import jooq.db.tables.records.PersonRecord;
import org.mapstruct.Mapper;
import ru.bankoperations.dto.PersonDto;

@Mapper(componentModel = "spring")
public interface PersonDtoPersonRecordMapping {
    PersonRecord personDtoToPersonRecord(PersonDto personDto);
    PersonDto personRecordToPersonDto(PersonRecord personRecord);
}
