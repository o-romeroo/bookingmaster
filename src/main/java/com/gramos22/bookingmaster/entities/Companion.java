package com.gramos22.bookingmaster.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "companions")
@AllArgsConstructor
@RequiredArgsConstructor
public class Companion {
    @Id
    @Column(name = "companion_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "first_name", nullable = false)
    private String first_name;
    @Column(name = "last_name", nullable = false)
    private String last_name;
    @Column(name = "document", nullable = false)
    private String document;
}
