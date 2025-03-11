package com.side.football_project.domain.team.entity;

import com.side.football_project.domain.user.entity.User;
import com.side.football_project.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@Entity
@DynamicUpdate
public class Team extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 25)
    private String teamName;

    private int headCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    /**
     * 생성자
     */
    protected Team() {}

    @Builder
    public Team(String teamName, int headCount, User user) {
        this.teamName = teamName;
        this.headCount = headCount;
        this.user = user;
    }

    /**
     * 서비스 편의 메서드
     */
    public void updateTeamInfo(String teamName, int headCount) {
        this.teamName = teamName;
        this.headCount = headCount;
    }
}
