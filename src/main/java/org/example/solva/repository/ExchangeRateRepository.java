package org.example.solva.repository;

import org.example.solva.entity.ExchangeRate;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

public interface ExchangeRateRepository extends ReactiveCrudRepository<ExchangeRate, Long> {
    Mono<ExchangeRate> findTopByCurrencyPairOrderByDateDesc(String currencyPair);
    Mono<Boolean> existsByCurrencyPairAndDate(String currencyPair, LocalDate date);
}
