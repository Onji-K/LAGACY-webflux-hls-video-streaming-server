package com.oj.videostreamingserver.global.util;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *     @Test
 *     void readsAllEn(){
 *         repository.save(new DraftVideo("/ttke/dd", 1L))
 *                 .as(Transaction::withRollBack)
 *                         .as(StepVerifier::create)
 *                 .expectNextCount(1)
 *                                 .verifyComplete();
 *
 *         repository.findAll().log()
 *                 .as(StepVerifier::create)
 *                 .expectNextCount(7)
 *                 .verifyComplete();
 */
@Component
public class Transaction {
    private static TransactionalOperator operator;

    @Autowired
    protected Transaction(final TransactionalOperator operator) {
        Transaction.operator = operator;
    }

    public static <T> Mono<T> withRollBack(final Mono<T> publisher){
        return operator.execute(tx->{
            tx.setRollbackOnly();
            return publisher;
        }).next();
    }

    public static <T> Flux<T> withRollBack(final Flux<T> publisher){
        return operator.execute(tx->{
            tx.setRollbackOnly();
            return publisher;
        });
    }
}
