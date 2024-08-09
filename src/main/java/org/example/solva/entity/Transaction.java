package org.example.solva.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Table("transactions")
@RequiredArgsConstructor
@NoArgsConstructor
@Schema(description = "Модель данных транзакций")
public class Transaction {
    @Id
    @Schema(description = "Идентификатор транзакции", example = "1", required = false, hidden = true)
    private Long id;
    @NonNull
    @Schema(description = "Идентификатор клиента", example = "14534", required = true)
    private Long accountFrom;
    @NonNull
    @Schema(description = "Идентификатор получателя", example = "153453", required = true)
    private Long accountTo;
    @NonNull
    @Schema(description = "Валюта транзакции", example = "USD", required = true)
    private String currencyShortname;
    @NonNull
    @Schema(description = "Сумма транзакции", example = "1542.56", required = true)
    private BigDecimal sum;
    @NonNull
    @Schema(description = "Категория расходов", example = "PRODUCT", required = true)
    private Category expenseCategory;
    @Schema(description = "Время транзакции", example = "2024-08-09T06:58:47.838Z", required = false, hidden = true)
    private LocalDateTime datetime;
    @Schema(description = "Исчерпан ли лимит после выполнения транзакции", example = "true", required = false, hidden = true)
    private boolean limitExceeded;
}