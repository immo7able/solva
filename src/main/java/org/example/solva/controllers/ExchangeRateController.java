package org.example.solva.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.solva.entity.ExchangeRate;
import org.example.solva.service.ExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
@Tag(name = "ExchangeRateController", description = "Контроллер для курсов валют")
@RestController
@RequestMapping("/api/exchange-rates")
public class ExchangeRateController {

    @Autowired
    private ExchangeRateService exchangeRateService;
    @Operation(summary = "Добавить курс валют", description = "Добавляет курс валют в бд")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное добавление курса")
    })
    @PostMapping(value = "/{currency1}/{currency2}")
    public Mono<ResponseEntity<Void>> fetchAndSaveExchangeRate(@PathVariable String currency1, @PathVariable String currency2) {
        return exchangeRateService.fetchAndSaveExchangeRate(currency1+"/"+ currency2)
                .then(Mono.just(ResponseEntity.ok().build()));
    }
    @Operation(summary = "Получить свежий курс", description = "Получает свежий курс выбранной валюты")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение курса")
    })
    @GetMapping(value = "/{currency1}/{currency2}/latest")
    public Mono<ResponseEntity<ExchangeRate>> getLastExchangeRate(@PathVariable String currency1, @PathVariable String currency2) {
        return exchangeRateService.getLastExchangeRate(currency1+"/"+currency2)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}