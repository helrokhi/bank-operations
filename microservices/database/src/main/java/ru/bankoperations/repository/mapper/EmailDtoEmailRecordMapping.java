package ru.bankoperations.repository.mapper;

import jooq.db.tables.records.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.bankoperations.dto.*;

@Mapper(componentModel = "spring")
public interface EmailDtoEmailRecordMapping {
    EmailDto emailRecordToDto(EmailRecord emailRecord);

    @Mapping(target = "id", ignore = true)
    EmailRecord emailDtoToRecord(EmailDto emailDto);
}
