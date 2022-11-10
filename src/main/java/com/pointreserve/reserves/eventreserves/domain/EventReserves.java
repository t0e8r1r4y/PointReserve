package com.pointreserve.reserves.eventreserves.domain;

import com.pointreserve.reserves.common.domain.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Entity
@Table(name = "EVENT_RESERVES")
public class EventReserves extends BaseTimeEntity {
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
    ReservesStatus status;


    @Builder
    public EventReserves(Long memberId, int amount,
                         ReservesStatus status) {
        this.memberId = memberId;
        this.amount = amount;
        this.status = status;
    }
}