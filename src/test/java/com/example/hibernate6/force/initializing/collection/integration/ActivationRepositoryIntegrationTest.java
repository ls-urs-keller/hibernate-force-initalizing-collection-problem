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
import java.util.function.Supplier;

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
        inTransaction( this::createAndRetrieve );
    }

    @Test

        // it is enough to set BankAccount#legalEntity to lazy and this will pass
        // but this seems to be regression in cycle detection
    void using_separate_transactions() {
        createAndRetrieve();
    }

    void createAndRetrieve() {
        var le = new LegalEntity();
        inTransaction(() -> em.persist(le));
        var bankAccount = new BankAccount(null, le);
        inTransaction(() -> em.persist(bankAccount));
        var ownership = new Ownership(null, le);
        inTransaction(() -> em.persist(ownership));
        Set<BankAccount> bankAccounts = new HashSet<>();
        bankAccounts.add(bankAccount);
        le.setBankAccounts(bankAccounts);
        le.setOwnership(ownership);
        inTransaction(() -> em.merge(le));
        var activation = new Activation(null, le, bankAccount);
        inTransaction(() -> em.persist(activation));
        Optional<Activation> activation0 = Optional.ofNullable(inTransaction(() -> em.find(Activation.class, activation.getId())));
        assertTrue(activation0.isPresent());
    }


    public <T> T inTransaction(Supplier<T> function) {
        transactionTemplate.setPropagationBehavior( TransactionTemplate.PROPAGATION_REQUIRED );
        return  transactionTemplate.execute(status -> function.get());
    }

    public void inTransaction(Runnable runnable) {
        inTransaction(() -> {
            runnable.run();
            return null;
        });
    }

}
