package org.example.solva.repository;

import org.example.solva.entity.Category;
import org.example.solva.entity.DTO.TransactionDTO;
import org.example.solva.entity.Transaction;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;


public interface TransactionRepository extends ReactiveCrudRepository<Transaction, Long> {
    @Query("SELECT t.*, l.limit_sum, l.limit_datetime, l.limit_currency_shortname FROM transactions t JOIN limits l ON t.datetime > l.limit_datetime WHERE t.limit_exceeded = true AND l.limit_datetime = (SELECT MAX(l2.limit_datetime) FROM limits l2 WHERE l2.limit_datetime <= t.datetime)")
    Flux<TransactionDTO> findAllWithLimitExceeded();
    Flux<Transaction> findAllByExpenseCategory(Category category);
}
