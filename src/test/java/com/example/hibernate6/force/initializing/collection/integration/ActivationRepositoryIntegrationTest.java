package com.example.hibernate6.force.initializing.collection.integration;

import com.example.hibernate6.force.initializing.collection.models.*;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@SpringBootTest
class ActivationRepositoryIntegrationTest {
    @Autowired
    EntityManager em;

    @Autowired
    TransactionTemplate transactionTemplate;

    @Test
    void in_one_transaction() {
        tx(() -> common(this::persist,
                this::persist,
                this::persist,
                this::persist,
                id -> Optional.ofNullable(em.find(Activation.class, id))));
    }

    @Test

        // it is enough to set BankAccount#legalEntity to lazy and this will pass
        // but this seems to be regression in cycle detection
    void using_separate_transactions() {
        common(tx(this::persist),
                tx(this::persist),
                tx(this::persist),
                tx(this::persist),
                tx(id -> Optional.ofNullable(em.find(Activation.class, id))));
    }

    void common(Function<LegalEntity, LegalEntity> legalEntityRepo,
                Function<BankAccount, BankAccount> bankAccountRepo,
                Function<Ownership, Ownership> ownershipRepo,
                Function<Activation, Activation> activationRepo,
                Function<Long, Optional<Activation>> findActivation
    ) {
        var le = legalEntityRepo.apply(new LegalEntity());
        var bankAccount = bankAccountRepo.apply(new BankAccount(null, le));
        var ownerShip = ownershipRepo.apply(new Ownership(null, le));
        Set<BankAccount> bankAccounts = new HashSet<>();
        bankAccounts.add(bankAccount);
        le.setBankAccounts(bankAccounts);
        le.setOwnership(ownerShip);
        legalEntityRepo.apply(le);
        var activation = activationRepo.apply(new Activation(null, le, bankAccount));
        Optional<Activation> activation0 = findActivation.apply(activation.getId());
        assertTrue(activation0.isPresent());
    }


    public <L, R> Function<L, R> tx(Function<L, R> function) {
        return l -> transactionTemplate.execute(status -> function.apply(l));
    }

    public <L, R> Function<L, R> tx(Runnable runnable) {
        return tx(ignored -> {
            runnable.run();
            return null;
        });
    }

    <T extends WithId> T persist(T e) {
        if (e.getId() != null) {
            em.merge(e);
        } else {
            em.persist(e);
        }
        return e;
    }

}
