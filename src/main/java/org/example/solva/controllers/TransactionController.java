package org.example.solva.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.solva.entity.DTO.TransactionDTO;
import org.example.solva.entity.Transaction;
import org.example.solva.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
@Tag(name = "TransactionController", description = "Контроллер для транзакций")
@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;
    @Operation(summary = "Создать новую транзакцию", description = "Создает новую транзакцию с указанными данными")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное создание транзакции")
    })
    @PostMapping
    public Mono<ResponseEntity<Transaction>> createTransaction(@RequestBody Transaction transaction) {
        return transactionService.saveTransaction(transaction)
                .map(savedTransaction -> ResponseEntity.ok(savedTransaction))
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }
    @Operation(summary = "Получить список транзакций", description = "Получает список транзакций, превысивших установленный лимит")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение списка транзакций")
    })
    @GetMapping("/exceeded")
    public Flux<TransactionDTO> getExceededTransactions() {
        return transactionService.getExceededTransactions();
    }
}