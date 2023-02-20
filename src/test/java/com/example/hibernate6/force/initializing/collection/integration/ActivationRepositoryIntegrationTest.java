package com.example.hibernate6.force.initializing.collection.integration;

import com.example.hibernate6.force.initializing.collection.models.*;

import org.junit.jupiter.api.Test;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertNotNull;

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
			var activation = new Activation(null, le, bankAccount);
			session.persist(activation);
			session.flush();
			session.clear();
			assertNotNull(session.find(Activation.class, activation.getId()));
		} );
    }

    public void inTransaction(Consumer<Session> action) {
		transactionTemplate.execute( status -> {
			action.accept( em.unwrap( Session.class ) );
			return null;
		} );
    }

}
