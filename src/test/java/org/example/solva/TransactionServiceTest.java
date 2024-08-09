package org.example.solva;

import org.example.solva.entity.Category;
import org.example.solva.entity.ExchangeRate;
import org.example.solva.entity.Limit;
import org.example.solva.entity.Transaction;
import org.example.solva.repository.ExchangeRateRepository;
import org.example.solva.repository.LimitRepository;
import org.example.solva.repository.TransactionRepository;
import org.example.solva.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private LimitRepository limitRepository;

    @Mock
    private ExchangeRateRepository exchangeRateRepository;

    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLimitExceededWhenTotalTransactionsExceedLimit() {
        Transaction transaction = new Transaction();
        transaction.setAccountFrom(1L);
        transaction.setAccountTo(2L);
        transaction.setDatetime(LocalDateTime.now());
        transaction.setCurrencyShortname("KZT");
        transaction.setSum(BigDecimal.valueOf(500000));
        transaction.setExpenseCategory(Category.SERVICE);

        Limit limit = new Limit();
        limit.setLimitSum(BigDecimal.valueOf(1000));
        limit.setLimitDatetime(LocalDateTime.now());
        limit.setLimitCurrencyShortname("USD");
        limit.setExpenseCategory(Category.SERVICE);

        ExchangeRate usdExchangeRate = new ExchangeRate("USD/KZT", LocalDate.now(), BigDecimal.ONE);

        when(limitRepository.findTopByExpenseCategoryOrderByLimitDatetimeDesc(Category.SERVICE))
                .thenReturn(Mono.just(limit));

        when(exchangeRateRepository.findTopByCurrencyPairOrderByDateDesc("USD/KZT"))
                .thenReturn(Mono.just(usdExchangeRate));

        when(transactionRepository.findAllByExpenseCategory(Category.SERVICE))
                .thenReturn(Flux.just(
                        transaction,
                        new Transaction(1L, 2L, "KZT", BigDecimal.valueOf(300000), Category.SERVICE)
                ));

        when(transactionRepository.save(any(Transaction.class)))
                .thenReturn(Mono.just(transaction));

        Mono<Transaction> result = transactionService.saveTransaction(transaction);

        result.subscribe(t -> {
            assertTrue(t.isLimitExceeded(), "Флаг limitExceeded должен быть true");
        });

        verify(transactionRepository, times(1)).save(transaction);
    }

    @Test
    void testLimitNotExceededWhenTotalTransactionsUnderLimit() {
        Transaction transaction = new Transaction();
        transaction.setAccountFrom(1L);
        transaction.setAccountTo(2L);
        transaction.setDatetime(LocalDateTime.now());
        transaction.setCurrencyShortname("KZT");
        transaction.setSum(BigDecimal.valueOf(50000));
        transaction.setExpenseCategory(Category.SERVICE);

        Limit limit = new Limit();
        limit.setLimitSum(BigDecimal.valueOf(1000));
        limit.setLimitDatetime(LocalDateTime.now());
        limit.setLimitCurrencyShortname("USD");
        limit.setExpenseCategory(Category.SERVICE);

        ExchangeRate usdExchangeRate = new ExchangeRate("USD/KZT", LocalDate.now(), BigDecimal.valueOf(477));

        when(limitRepository.findTopByExpenseCategoryOrderByLimitDatetimeDesc(Category.SERVICE))
                .thenReturn(Mono.just(limit));

        when(exchangeRateRepository.findTopByCurrencyPairOrderByDateDesc("USD/KZT"))
                .thenReturn(Mono.just(usdExchangeRate));

        when(transactionRepository.findAllByExpenseCategory(Category.SERVICE))
                .thenReturn(Flux.just(
                        transaction,
                        new Transaction(1L, 2L, "KZT", BigDecimal.valueOf(300000), Category.SERVICE)
                        )
                );

        when(transactionRepository.save(any(Transaction.class)))
                .thenReturn(Mono.just(transaction));

        Mono<Transaction> result = transactionService.saveTransaction(transaction);

        result.subscribe(t -> {
            assertFalse(t.isLimitExceeded(), "Флаг limitExceeded должен быть false");
        });

        verify(transactionRepository, times(1)).save(transaction);
    }
}