package org.example.solva.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.solva.entity.Limit;
import org.example.solva.service.LimitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
@Tag(name = "LimitController", description = "Контроллер для лимитов")
@RestController
@RequestMapping("/api/limits")
public class LimitController {
    @Autowired
    private LimitService limitService;
    @Operation(summary = "Добавить лимит", description = "Добавляет лимит на транзакции в бд")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное добавление лимита")
    })
    @PostMapping
    public Mono<ResponseEntity<Limit>> setNewLimit(@RequestBody Limit limit) {
        return limitService.setNewLimit(limit)
                .map(savedLimit -> ResponseEntity.ok(savedLimit))
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }
    @Operation(summary = "Получить все лимиты", description = "Получает список всех установленных лимитов")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение списка")
    })
    @GetMapping
    public Flux<Limit> getAllLimits() {
        return limitService.getAllLimits();
    }
}