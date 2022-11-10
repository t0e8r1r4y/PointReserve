package com.pointreserve.reserves.accumulationpoint.infra;

import com.pointreserve.reserves.accumulationpoint.domain.AccumulatedPoint;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import javax.persistence.LockModeType;
import java.util.Optional;

import static com.pointreserve.reserves.accumulationpoint.domain.QAccumulatedPoint.accumulatedPoint;

@RequiredArgsConstructor
public class AccumulatedPointRespositoryImpl implements AccountRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public Optional<AccumulatedPoint> getByMemberId(Long memberId) {
        return Optional.of(jpaQueryFactory.selectFrom(accumulatedPoint)
                .where(accumulatedPoint.memberId.eq(memberId))
                .setLockMode(LockModeType.PESSIMISTIC_READ)
                .fetchOne());
    }
}
