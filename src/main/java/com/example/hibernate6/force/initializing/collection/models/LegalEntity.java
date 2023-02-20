package com.example.hibernate6.force.initializing.collection.models;


import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class LegalEntity {
    @Id
    @GeneratedValue
    private Long id;

    @OneToOne(mappedBy = "legalEntity", fetch = FetchType.EAGER)
    private Ownership ownership;

    @OneToMany(mappedBy = "legalEntity", fetch = FetchType.EAGER)
    private Set<BankAccount> bankAccounts = new HashSet<>();

    public LegalEntity() {
    }

    public LegalEntity(Long id, Ownership ownership, Set<BankAccount> bankAccounts) {
        this.id = id;
        this.ownership = ownership;
        this.bankAccounts = bankAccounts;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Ownership getOwnership() {
        return ownership;
    }

    public void setOwnership(Ownership ownership) {
        this.ownership = ownership;
    }

    public Set<BankAccount> getBankAccounts() {
        return bankAccounts;
    }

    public void setBankAccounts(
            Set<BankAccount> bankAccounts) {
        this.bankAccounts = bankAccounts;
    }
}
