package com.example.hibernate6.force.initializing.collection.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Ownership {
    @Id
    @GeneratedValue
    Long id;
    @OneToOne(fetch = FetchType.EAGER)
    @NonNull
    LegalEntity legalEntity;
}
