package ru.bankoperations.repository.mapper;

import jooq.db.tables.records.*;
import org.mapstruct.Mapper;
import ru.bankoperations.dto.*;

@Mapper(componentModel = "spring")
public interface UserAuthDtoUserAuthRecordMapping {
    UserAuthRecord userAuthDtoToUserAuthRecord(UserAuthDto userAuthDto);
    UserAuthDto userAuthRecordToUserAuthDto(UserAuthRecord userAuthRecord);
}
