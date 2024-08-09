package org.example.solva.service;

import org.example.solva.entity.DTO.TransactionDTO;
import org.example.solva.entity.ExchangeRate;
import org.example.solva.entity.Limit;
import org.example.solva.entity.Transaction;
import org.example.solva.repository.ExchangeRateRepository;
import org.example.solva.repository.LimitRepository;
import org.example.solva.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final LimitRepository limitRepository;
    private final ExchangeRateRepository exchangeRateRepository;
    @Autowired
    public TransactionService(TransactionRepository transactionRepository, LimitRepository limitRepository, ExchangeRateRepository exchangeRateRepository) {
        this.transactionRepository = transactionRepository;
        this.limitRepository = limitRepository;
        this.exchangeRateRepository = exchangeRateRepository;
    }

    public Mono<Transaction> saveTransaction(Transaction transaction) {
        return limitRepository.findTopByExpenseCategoryOrderByLimitDatetimeDesc(transaction.getExpenseCategory())
                .switchIfEmpty(Mono.defer(() -> {
                    Limit defaultLimit = new Limit();
                    defaultLimit.setLimitSum(BigDecimal.valueOf(1000));
                    defaultLimit.setLimitDatetime(LocalDateTime.now());
                    defaultLimit.setLimitCurrencyShortname("USD");
                    defaultLimit.setExpenseCategory(transaction.getExpenseCategory());
                    return limitRepository.save(defaultLimit).thenReturn(defaultLimit);
                }))
                .flatMap(currentLimit -> {
                    BigDecimal limitSum = currentLimit.getLimitSum();
                    return exchangeRateRepository.findTopByCurrencyPairOrderByDateDesc("USD/" + transaction.getCurrencyShortname())
                            .defaultIfEmpty(new ExchangeRate("USD/" + transaction.getCurrencyShortname(), LocalDate.now(), BigDecimal.ONE))
                            .flatMap(currentExchangeRate -> {
                                BigDecimal transactionAmountInUSD = convertToUSD(transaction.getSum(), transaction.getCurrencyShortname(), currentExchangeRate);
                                return transactionRepository.findAllByExpenseCategory(transaction.getExpenseCategory())
                                        .flatMap(existingTransaction -> {
                                            if ("USD".equalsIgnoreCase(existingTransaction.getCurrencyShortname())) {
                                                return Mono.just(existingTransaction.getSum());
                                            } else {
                                                return exchangeRateRepository.findTopByCurrencyPairOrderByDateDesc("USD/" + existingTransaction.getCurrencyShortname())
                                                        .map(exchangeRate -> existingTransaction.getSum().divide(exchangeRate.getCloseRate(), 2, BigDecimal.ROUND_HALF_UP))
                                                        .defaultIfEmpty(BigDecimal.ZERO);
                                            }
                                        })
                                        .collectList()
                                        .map(sums -> sums.stream().reduce(BigDecimal.ZERO, BigDecimal::add))
                                        .flatMap(totalTransactionSum -> {
                                            totalTransactionSum = totalTransactionSum.add(transactionAmountInUSD);
                                            if (totalTransactionSum.compareTo(limitSum) > 0) {
                                                transaction.setLimitExceeded(true);
                                            }
                                            transaction.setDatetime(LocalDateTime.now());
                                            return transactionRepository.save(transaction);
                                        });
                            });
                });
    }


    private BigDecimal convertToUSD(BigDecimal amount, String currencyShortname, ExchangeRate exchangeRate) {
        if ("USD".equalsIgnoreCase(currencyShortname)) {
            return amount;
        }
        return amount.divide(exchangeRate.getCloseRate(), 2, BigDecimal.ROUND_HALF_UP);
    }

    public Flux<TransactionDTO> getExceededTransactions() {
        return transactionRepository.findAllWithLimitExceeded();
    }
}
