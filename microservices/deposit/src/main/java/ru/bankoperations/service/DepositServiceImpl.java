package ru.bankoperations.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.bankoperations.client.*;
import ru.bankoperations.processor.Processor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@Service
@RequiredArgsConstructor()
@Slf4j
public class DepositServiceImpl implements DepositService{
    private final DatabaseClient databaseClient;
    private static final String CRON = "*/60 * * * * *";

    private static final Set<Processor> siteThreadSet = new HashSet<>(0);

    private final ThreadPoolExecutor fixedThreadPool =
            (ThreadPoolExecutor) Executors.newFixedThreadPool(numberOfProcessorCores());


    @Override
    @Scheduled(cron = CRON)
    public void updateAllDepositsEveryMinute() {
        taskStart();
    }

    private void taskStart() {
        clearSiteThreadSet();
        List<Long> allPersonId = databaseClient.findAllPersonId();
        log.info("size allPersonId {}", allPersonId.size());

        for (Long personId : allPersonId) {
            Processor processor = new Processor(personId, databaseClient);
            siteThreadSet.add(processor);
            fixedThreadPool.execute(processor);
        }
    }

    public static void clearSiteThreadSet() {
        siteThreadSet.clear();
    }

    private int numberOfProcessorCores() {
        return Runtime.getRuntime().availableProcessors();
    }
}
