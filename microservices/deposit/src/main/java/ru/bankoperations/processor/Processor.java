package ru.bankoperations.processor;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import ru.bankoperations.client.DatabaseClient;

@Getter
@Slf4j
public class Processor extends Thread implements Runnable{
    private final Long personId;
    private final DatabaseClient databaseClient;
    public Processor(
            Long personId,
            DatabaseClient databaseClient) {
        this.personId = personId;
        this.databaseClient = databaseClient;
    }

    @Override
    public void run() {
        log.info("Processor run person {}", personId);
        databaseClient.updateDeposit(personId);
    }
}
