package com.acheron.testtask.repository;

import com.acheron.testtask.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByEmail(String email);

    Optional<User> findUserById(Long id);

    @Query("select u from User u where u.birthDate between :start and :end")
    List<User> findUsersByBirthDateBetween(LocalDate start, LocalDate end);
}
