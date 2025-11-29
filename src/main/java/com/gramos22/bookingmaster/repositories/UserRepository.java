package com.gramos22.bookingmaster.repositories;

import com.gramos22.bookingmaster.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
}
