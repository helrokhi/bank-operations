package ru.bankoperations.repository.mapper;

import jooq.db.tables.records.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.bankoperations.dto.*;

@Mapper(componentModel = "spring")
public interface DepositDtoDepositRecordMapping {
    DepositDto depositRecordToDto(DepositRecord depositRecord);

    @Mapping(target = "id", ignore = true)
    DepositRecord depositDtoToRecord(DepositDto depositDto);
}
