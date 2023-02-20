package com.example.hibernate6.force.initializing.collection.integration;

import com.example.hibernate6.force.initializing.collection.models.*;

import org.junit.jupiter.api.Test;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.hibernate.Session;

@ActiveProfiles("test")
@SpringBootTest
class ActivationRepositoryIntegrationTest {
    @Autowired
    EntityManager em;

    @Autowired
    TransactionTemplate transactionTemplate;

    @Test
    void in_one_transaction() {
        inTransaction( session -> {
			var le = new LegalEntity();
			session.persist(le);
			var bankAccount = new BankAccount(null, le);
			session.persist(bankAccount);
			var ownership = new Ownership(null, le);
			session.persist(ownership);
			Set<BankAccount> bankAccounts = new HashSet<>();
			bankAccounts.add(bankAccount);
			le.setBankAccounts(bankAccounts);
			le.setOwnership(ownership);
			session.merge(le);
			var activation = new Activation(null, le, bankAccount);
			session.persist(activation);
			Optional<Activation> activation0 = Optional.ofNullable(session.find(Activation.class, activation.getId()));
			assertTrue(activation0.isPresent());
		} );
    }

    @Test

        // it is enough to set BankAccount#legalEntity to lazy and this will pass
        // but this seems to be regression in cycle detection
    void using_separate_transactions() {
		var le = new LegalEntity();
		inTransaction(session -> session.persist(le));
		var bankAccount = new BankAccount(null, le);
		inTransaction(session -> session.persist(bankAccount));
		var ownership = new Ownership(null, le);
		inTransaction(session -> session.persist(ownership));
		Set<BankAccount> bankAccounts = new HashSet<>();
		bankAccounts.add(bankAccount);
		le.setBankAccounts(bankAccounts);
		le.setOwnership(ownership);
		inTransaction(session -> session.merge(le));
		var activation = new Activation(null, le, bankAccount);
		inTransaction(session -> session.persist(activation));
		Optional<Activation> activation0 = Optional.ofNullable(inTransactionReturning(session -> session.find(Activation.class, activation.getId())));
		assertTrue(activation0.isPresent());
    }

    void createAndRetrieve() {
        var le = new LegalEntity();
        inTransaction(session -> session.persist(le));
        var bankAccount = new BankAccount(null, le);
        inTransaction(session -> session.persist(bankAccount));
        var ownership = new Ownership(null, le);
        inTransaction(session -> session.persist(ownership));
        Set<BankAccount> bankAccounts = new HashSet<>();
        bankAccounts.add(bankAccount);
        le.setBankAccounts(bankAccounts);
        le.setOwnership(ownership);
        inTransaction(session -> session.merge(le));
        var activation = new Activation(null, le, bankAccount);
        inTransaction(session -> session.persist(activation));
        Optional<Activation> activation0 = Optional.ofNullable(inTransactionReturning(session -> session.find(Activation.class, activation.getId())));
        assertTrue(activation0.isPresent());
    }


    public <T> T inTransactionReturning(Function<Session, T> action) {
        transactionTemplate.setPropagationBehavior( TransactionTemplate.PROPAGATION_REQUIRED );
        return  transactionTemplate.execute(status -> action.apply(em.unwrap(Session.class)));
    }

    public void inTransaction(Consumer<Session> action) {
        inTransactionReturning(session -> {
            action.accept(session);
            return null;
        });
    }

    public void inTransaction(Runnable action) {
        inTransactionReturning(session -> {
            action.run();
            return null;
        });
    }

}
