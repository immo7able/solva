package org.example.solva.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Table("exchange_rate")
@RequiredArgsConstructor
@NoArgsConstructor
@Schema(description = "Модель данных обменного курса")
public class ExchangeRate {
    @Id
    @Schema(description = "Идентификатор валютной пары", example = "1", required = false, hidden = true)
    private Long id;
    @NonNull
    @Schema(description = "Валютная пара", example = "USD/KZT", required = true)
    private String currencyPair;
    @NonNull
    @Schema(description = "Дата курса валютной пары", example = "2024-08-09", required = false, hidden = true)
    private LocalDate date;
    @NonNull
    @Schema(description = "Курс на закрытие", example = "135.32", required = false, hidden = true)
    private BigDecimal closeRate;

}
