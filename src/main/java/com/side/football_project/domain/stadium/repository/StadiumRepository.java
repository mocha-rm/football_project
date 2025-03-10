package com.side.football_project.domain.stadium.repository;

import com.side.football_project.domain.stadium.entity.Stadium;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StadiumRepository extends JpaRepository<Stadium, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select st from Stadium st where st.id =:id")
    Optional<Stadium> findByIdWithLock(@Param("id") Long id);
}
