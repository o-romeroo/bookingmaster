package com.gramos22.bookingmaster.repositories;

import com.gramos22.bookingmaster.entities.Companion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanionRepository extends JpaRepository<Companion, Integer> {
}
