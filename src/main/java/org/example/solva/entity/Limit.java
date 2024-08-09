package org.example.solva.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Table("limits")
@Schema(description = "Модель данных лимитов пользователя")
public class Limit {
    @Id
    @Schema(description = "Идентификатор лимита", example = "1", required = false, hidden = true)
    private Long id;
    @Schema(description = "Сумма лимита", example = "1435.54", required = true)
    private BigDecimal limitSum;
    @Schema(description = "Время установки лимита", example = "2024-08-09T06:58:47.838Z", required = false, hidden = true)
    private LocalDateTime limitDatetime;
    @Schema(description = "Валюта установленного лимита", example = "USD", required = false, hidden = true)
    private String limitCurrencyShortname;
    @Schema(description = "Категория расходов", example = "SERVICE", required = true)
    private Category expenseCategory;
}