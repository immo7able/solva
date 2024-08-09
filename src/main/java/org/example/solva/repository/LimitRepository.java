package org.example.solva.repository;

import org.example.solva.entity.Category;
import org.example.solva.entity.Limit;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface LimitRepository extends ReactiveCrudRepository<Limit, Long> {
    Mono<Limit> findTopByExpenseCategoryOrderByLimitDatetimeDesc(Category category);
}
