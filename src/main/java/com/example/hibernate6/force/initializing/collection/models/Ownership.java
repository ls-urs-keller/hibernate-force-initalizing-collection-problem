package com.example.hibernate6.force.initializing.collection.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
public class Ownership {
    @Id
    @GeneratedValue
    private Long id;
    @OneToOne
    private LegalEntity legalEntity;

    public Ownership() {
    }

    public Ownership(Long id, LegalEntity legalEntity) {
        this.id = id;
        this.legalEntity = legalEntity;
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
}
