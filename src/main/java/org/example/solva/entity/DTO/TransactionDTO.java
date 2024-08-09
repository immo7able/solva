package org.example.solva.entity.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.example.solva.entity.Category;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
@Schema(description = "Модель данных для передачи транзакций, превысивших установленный лимит")
public class TransactionDTO {
    @Schema(description = "Идентификатор клиента", example = "14534")
    private Long accountFrom;
    @Schema(description = "Идентификатор получателя", example = "153453")
    private Long accountTo;
    @Schema(description = "Валюта транзакции", example = "USD")
    private String currencyShortname;
    @Schema(description = "Сумма транзакции", example = "1542.56")
    private BigDecimal sum;
    @Schema(description = "Категория расходов", example = "PRODUCT")
    private Category expenseCategory;
    @Schema(description = "Время транзакции", example = "2024-08-09T06:58:47.838Z")
    private LocalDateTime datetime;
    @Schema(description = "Сумма лимита", example = "1435.54")
    private BigDecimal limitSum;
    @Schema(description = "Время установки лимита", example = "2024-08-09T06:58:47.838Z")
    private LocalDateTime limitDatetime;
    @Schema(description = "Валюта установленного лимита", example = "USD")
    private String limitCurrencyShortname;
}
