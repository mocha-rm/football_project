package com.side.football_project.domain.shorts.repository;

import com.side.football_project.domain.shorts.entity.Shorts;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShortsRepository extends JpaRepository<Shorts, Long> {
}
