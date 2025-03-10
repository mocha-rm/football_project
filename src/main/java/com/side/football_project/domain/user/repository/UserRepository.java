package com.side.football_project.domain.user.repository;

import com.side.football_project.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query("select avg(mu.rate) from MatchUser mu where mu.user.id = :userId")
    Double findAvgRateByUserId(Long userId);

}
