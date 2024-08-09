package org.example.solva;

import org.example.solva.entity.Category;
import org.example.solva.entity.Limit;
import org.example.solva.entity.Transaction;
import org.example.solva.repository.ExchangeRateRepository;
import org.example.solva.repository.LimitRepository;
import org.example.solva.repository.TransactionRepository;
import org.example.solva.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class TransactionServiceIntegrationTest {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private LimitRepository limitRepository;

    @Autowired
    private ExchangeRateRepository exchangeRateRepository;

    @Test
    void testLimitExceededIntegration() {
        Transaction transaction = new Transaction();
        transaction.setAccountFrom(1L);
        transaction.setAccountTo(2L);
        transaction.setDatetime(LocalDateTime.now());
        transaction.setCurrencyShortname("USD");
        transaction.setSum(BigDecimal.valueOf(1500));
        transaction.setExpenseCategory(Category.SERVICE);

        Limit limit = new Limit();
        limit.setLimitSum(BigDecimal.valueOf(1000));
        limit.setLimitDatetime(LocalDateTime.now());
        limit.setLimitCurrencyShortname("USD");
        limit.setExpenseCategory(Category.SERVICE);

        limitRepository.save(limit).block();
        transactionRepository.save(transaction).block();

        Mono<Transaction> result = transactionService.saveTransaction(transaction);

        result.subscribe(t -> {
            assertTrue(t.isLimitExceeded(), "Флаг limitExceeded должен быть true");
        });
    }
}