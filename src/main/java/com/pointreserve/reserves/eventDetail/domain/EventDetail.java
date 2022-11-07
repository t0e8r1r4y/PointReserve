package com.pointreserve.reserves.eventDetail.domain;

import com.pointreserve.reserves.eventReserves.domain.ReservesStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Entity
@Table(name = "EVENT_DETAIL")
public class EventDetail {

    @Id
    @GeneratedValue(generator = "event-generator")
    @GenericGenerator(name = "event-generator",
            parameters = @Parameter(name = "prefix", value = "d"),
            strategy = "com.pointreserve.reserves.common.domain.CustomIdGenerator")
    String id;

    @Column()
    Long membershipId;

    @Column(nullable = false)
    ReservesStatus status;

    @Column(nullable = false)
    int amount;

    @Column(nullable = false)
    String eventId;

    @Column(nullable = true)
    String signUpId;

    @Column(nullable = true)
    String cancelId;

    @Column(nullable = false)
    LocalDateTime effectiveData;

    @Column(nullable = false)
    LocalDateTime expiryDate;

    @Builder
    public EventDetail( Long membershipId, ReservesStatus status, int amount, String eventId,
                        String signUpId, String cancelId, LocalDateTime effectiveData, LocalDateTime expiryDate){
        this.membershipId = membershipId;
        this.status = status;
        this.amount = amount;
        this.eventId = eventId;
        this.signUpId = signUpId;
        this.cancelId = cancelId;
        this.effectiveData = effectiveData;
        this.expiryDate = expiryDate;
    }

}
