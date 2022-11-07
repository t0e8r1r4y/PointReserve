package com.pointreserve.reserves.eventReserves.infra;

import com.pointreserve.reserves.eventReserves.domain.EventReserves;
import com.pointreserve.reserves.eventReserves.ui.dto.EventReservesSearch;
import com.pointreserve.reserves.eventReserves.domain.QEventReserves;
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
                .fetch();
    }
}
