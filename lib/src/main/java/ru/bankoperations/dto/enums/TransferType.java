package ru.bankoperations.dto.enums;

import lombok.Getter;

@Getter
public enum TransferType {
    DEPOSIT_PERCENT(1, "DEPOSIT_PERCENT", "Проценты на первоначальный депозит"),
    TAKE_MONEY_TRANSFER(2, "TAKE_MONEY_TRANSFER", "Списание средств"),
    PUT_MONEY_TRANSFER(3, "PUT_MONEY_TRANSFER", "Пополнение средств");
    private final int id;
    private final String code;
    private final String name;

    TransferType(int id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }
}
