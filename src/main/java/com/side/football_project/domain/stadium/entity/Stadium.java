package com.side.football_project.domain.stadium.entity;

import com.side.football_project.domain.user.entity.User;
import com.side.football_project.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor
public class Stadium extends BaseEntity {

    //최대 수용 인원 추가
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private StadiumStatus status;

    private String description;

    private Integer capacity;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Embedded
    private Address address;

    @Builder
    public Stadium(Long id, String name, StadiumStatus status, String description, Integer capacity, User user, Address address) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.description = description;
        this.capacity = capacity;
        this.user = user;
        this.address = address;
    }

    public void applyAddress(Address address) {
        this.address = address;
    }

    public boolean isFullyBooked() {
        return this.status.equals(StadiumStatus.NOT_POSSIBLE);
    }
}
