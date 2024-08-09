package org.example.solva.service;

import org.example.solva.entity.Limit;
import org.example.solva.repository.LimitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class LimitService {
    @Autowired
    private LimitRepository limitRepository;
    public Mono<Limit> setNewLimit(Limit limit) {
        if(limit.getLimitSum()==null)
            limit.setLimitSum(BigDecimal.valueOf(1000));
        limit.setLimitDatetime(LocalDateTime.now());
        limit.setLimitCurrencyShortname("USD");
        return limitRepository.save(limit);
    }
    public Flux<Limit> getAllLimits() {
        return limitRepository.findAll();
    }
}
