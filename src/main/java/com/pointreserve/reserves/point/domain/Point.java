package com.pointreserve.reserves.point.domain;

import com.pointreserve.reserves.common.domain.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "POINT")
public class Point extends BaseTimeEntity {
    @Id
    @GeneratedValue(generator = "event-generator")
    @GenericGenerator(name = "event-generator",
            parameters = @Parameter(name = "prefix", value = "e"),
            strategy = "com.pointreserve.reserves.common.domain.CustomIdGenerator")
    String id;

    @Column(nullable = false)
    Long memberId;

    @Column(nullable = false)
    int amount;

    @Column(nullable = false)
    PointStatus status;


    @Builder
    public Point(Long memberId, int amount,
                 PointStatus status) {
        this.memberId = memberId;
        this.amount = amount;
        this.status = status;
    }
}