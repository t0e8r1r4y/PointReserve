package com.pointreserve.reserves.eventreserves.infra;

import com.pointreserve.reserves.eventreserves.domain.EventReserves;
import com.pointreserve.reserves.eventreserves.ui.dto.EventReservesSearch;
import com.pointreserve.reserves.eventreserves.domain.QEventReserves;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class EventReservesRepositoryImpl implements EventReservesRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<EventReserves> getList(EventReservesSearch eventReservesSearch) {
        return jpaQueryFactory.selectFrom(QEventReserves.eventReserves)
                .where(QEventReserves.eventReserves.memberId.eq(eventReservesSearch.getMemberId()))
                .limit(eventReservesSearch.getSize())
                .offset(eventReservesSearch.getOffset())
                .orderBy(QEventReserves.eventReserves.effectiveData.asc())
                .fetch();
    }
}
