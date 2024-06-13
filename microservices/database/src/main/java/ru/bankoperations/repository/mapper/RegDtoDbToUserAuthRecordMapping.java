package ru.bankoperations.repository.mapper;

import jooq.db.tables.records.*;
import org.mapstruct.Mapper;
import ru.bankoperations.dto.RegDtoDb;

@Mapper(componentModel = "spring")
public interface RegDtoDbToUserAuthRecordMapping {
    UserAuthRecord regDtoDbToUserAuthRecord(RegDtoDb regDtoDb);
}
