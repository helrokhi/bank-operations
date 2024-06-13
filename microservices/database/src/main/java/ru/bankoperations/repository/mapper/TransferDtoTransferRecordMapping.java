package ru.bankoperations.repository.mapper;

import jooq.db.tables.records.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.bankoperations.dto.*;

@Mapper(componentModel = "spring")
public interface TransferDtoTransferRecordMapping {
    TransferDto transferRecordToDto(TransferRecord transferRecord);

    @Mapping(target = "id", ignore = true)
    TransferRecord transferDtoToRecord(TransferDto transferDto);
}
