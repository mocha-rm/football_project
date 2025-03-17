package com.side.football_project.domain.shorts.entity;

import com.side.football_project.global.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Shorts extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private String url;

    /**
     * 생성자
     */
    @Builder
    public Shorts(String title, String description, String url) {
        this.title = title;
        this.description = description;
        this.url = url;
    }
}
