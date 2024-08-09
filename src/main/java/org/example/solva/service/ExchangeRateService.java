package org.example.solva.service;

import jakarta.annotation.PostConstruct;
import org.example.solva.entity.ExchangeRate;
import org.example.solva.repository.ExchangeRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Service
public class ExchangeRateService {
    private final ExchangeRateRepository exchangeRateRepository;
    private final WebClient webClient;
    @Value("${api.key}")
    private String API_KEY;
    @Autowired
    public ExchangeRateService(WebClient.Builder webClientBuilder, ExchangeRateRepository exchangeRateRepository) {
        this.webClient = webClientBuilder.baseUrl("https://api.twelvedata.com").build();
        this.exchangeRateRepository = exchangeRateRepository;
    }
    @PostConstruct
    @Scheduled(cron = "0 0 0 * * *")
    public void fetchDailyExchangeRates() {
        Mono<Void> fetchKztUsd = fetchAndSaveExchangeRate("USD/KZT");
        Mono<Void> fetchRubUsd = fetchAndSaveExchangeRate("USD/RUB");
        Mono<Void> fetchEurUsd = fetchAndSaveExchangeRate("USD/EUR");
        Mono.when(fetchKztUsd, fetchRubUsd, fetchEurUsd).subscribe();
    }
    public Mono<Void> fetchAndSaveExchangeRate(String currencyPair) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/quote")
                        .queryParam("symbol", currencyPair)
                        .queryParam("apikey", API_KEY)
                        .build())
                .retrieve()
                .bodyToMono(Map.class)
                .flatMap(response -> {
                    String datetime = (String) response.get("datetime");
                    LocalDate date = LocalDate.parse(datetime);
                    return exchangeRateRepository.existsByCurrencyPairAndDate(currencyPair, date)
                            .flatMap(exists -> {
                                if (exists) {
                                    return Mono.empty();
                                }
                                ExchangeRate exchangeRate = new ExchangeRate();
                                exchangeRate.setCurrencyPair(currencyPair);
                                exchangeRate.setDate(date);
                                try {
                                    BigDecimal closeRate = new BigDecimal((String) response.get("close"));
                                    exchangeRate.setCloseRate(closeRate);
                                } catch (Exception e) {
                                    BigDecimal previousCloseRate = new BigDecimal((String) response.get("previous_close"));
                                    exchangeRate.setCloseRate(previousCloseRate);
                                }
                                return exchangeRateRepository.save(exchangeRate).then();
                            });
                });
    }

    public Mono<ExchangeRate> getLastExchangeRate(String currencyPair) {
        return exchangeRateRepository.findTopByCurrencyPairOrderByDateDesc(currencyPair);
    }
}
