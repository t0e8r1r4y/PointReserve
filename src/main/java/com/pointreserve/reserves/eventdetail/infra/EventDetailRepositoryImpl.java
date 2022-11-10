package com.pointreserve.reserves.eventdetail.infra;

import com.pointreserve.reserves.eventdetail.domain.EventDetail;
import static com.pointreserve.reserves.eventdetail.domain.QEventDetail.eventDetail;
import static com.pointreserve.reserves.eventreserves.domain.ReservesStatus.*;

import com.pointreserve.reserves.eventdetail.ui.dto.EventDetailSearch;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class EventDetailRepositoryImpl implements EventDetailRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<EventDetail> getListWithSignIdIsNotNull(Long memberId) {
        return jpaQueryFactory.selectFrom(eventDetail)
                .where(eventDetail.membershipId.eq(memberId))
                .where(eventDetail.signUpId.isNotNull())
                .fetch();
    }

    @Override
    public List<EventDetail> getListWithSignId(String signUpId) {
        return jpaQueryFactory.selectFrom(eventDetail)
                .where(eventDetail.signUpId.eq(signUpId))
                .fetch();
    }

    @Override
    public List<EventDetail> getListWithCancelId(String cancelId) {
        return jpaQueryFactory.selectFrom(eventDetail)
                .where(eventDetail.cancelId.eq(cancelId))
                .fetch();
    }

    @Override
    public List<EventDetail> getListNotInSignUpId(List<String> NotIn, Long memberId) {
        return jpaQueryFactory.selectFrom(eventDetail)
                .where(eventDetail.membershipId.eq(memberId))
                .where(eventDetail.id.notIn(NotIn))
                .where(eventDetail.status.ne(REDEEM))
                .fetch();
    }

    @Override
    public EventDetail getOneById(String id) {
        return jpaQueryFactory.selectFrom(eventDetail)
                .where(eventDetail.id.eq(id))
                .fetchOne();
    }

    @Override
    public List<EventDetail> getOneByEventId(String eventId) {
        return jpaQueryFactory.selectFrom(eventDetail)
                .where(eventDetail.eventId.eq(eventId))
                .fetch();
    }

    @Override
    public List<EventDetail> getListById(String id) {
        return jpaQueryFactory.selectFrom(eventDetail)
                .where(eventDetail.signUpId.isNotNull())
                .where(eventDetail.signUpId.eq(id))
                .fetch();
    }

    @Override
    public int sumAmount(Long memberId) {
        return jpaQueryFactory.select(eventDetail.amount.sum())
                .from(eventDetail)
                .where(eventDetail.membershipId.eq(memberId))
                .fetchOne();
    }

    @Override
    public List<EventDetail> getListByPage(EventDetailSearch eventDetailSearch) {
        return jpaQueryFactory.selectFrom(eventDetail)
                .where(eventDetail.membershipId.eq(eventDetailSearch.getMemberId()))
                .limit(eventDetailSearch.getSize())
                .offset(eventDetailSearch.getOffset())
                .fetch();
    }


}
