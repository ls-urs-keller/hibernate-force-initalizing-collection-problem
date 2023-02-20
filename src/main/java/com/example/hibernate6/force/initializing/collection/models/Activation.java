package com.example.hibernate6.force.initializing.collection.models;

import jakarta.persistence.*;

@Entity
public class Activation {
    @Id
    @GeneratedValue
    Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    LegalEntity legalEntity;

    @ManyToOne(fetch = FetchType.EAGER)
    BankAccount bankAccount;

    public Activation() {
    }

    public Activation(Long id, LegalEntity legalEntity, BankAccount bankAccount) {
        this.id = id;
        this.legalEntity = legalEntity;
        this.bankAccount = bankAccount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LegalEntity getLegalEntity() {
        return legalEntity;
    }

    public void setLegalEntity(LegalEntity legalEntity) {
        this.legalEntity = legalEntity;
    }

    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }
}
