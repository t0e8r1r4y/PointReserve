package com.pointreserve.reserves.pointdetail.infra;

import com.pointreserve.reserves.pointdetail.domain.PointDetail;
import static com.pointreserve.reserves.pointdetail.domain.QPointDetail.pointDetail;
import static com.pointreserve.reserves.point.domain.PointStatus.*;

import com.pointreserve.reserves.pointdetail.ui.dto.PointDetailSearch;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class PointDetailRepositoryImpl implements PointDetailRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<PointDetail> getListWithSignIdIsNotNull(Long memberId) {
        return jpaQueryFactory.selectFrom(pointDetail)
                .where(pointDetail.membershipId.eq(memberId))
                .where(pointDetail.signUpId.isNotNull())
                .fetch();
    }

    @Override
    public List<PointDetail> getListWithSignId(String signUpId) {
        return jpaQueryFactory.selectFrom(pointDetail)
                .where(pointDetail.signUpId.eq(signUpId))
                .fetch();
    }

    @Override
    public List<PointDetail> getListWithCancelId(String cancelId) {
        return jpaQueryFactory.selectFrom(pointDetail)
                .where(pointDetail.cancelId.eq(cancelId))
                .fetch();
    }

    @Override
    public List<PointDetail> getListNotInSignUpId(List<String> NotIn, Long memberId) {
        return jpaQueryFactory.selectFrom(pointDetail)
                .where(pointDetail.membershipId.eq(memberId))
                .where(pointDetail.id.notIn(NotIn))
                .where(pointDetail.status.ne(REDEEM))
                .fetch();
    }

    @Override
    public PointDetail getOneById(String id) {
        return jpaQueryFactory.selectFrom(pointDetail)
                .where(pointDetail.id.eq(id))
                .fetchOne();
    }

    @Override
    public List<PointDetail> getOneByEventId(String eventId) {
        return jpaQueryFactory.selectFrom(pointDetail)
                .where(pointDetail.eventId.eq(eventId))
                .fetch();
    }

    @Override
    public List<PointDetail> getListById(String id) {
        return jpaQueryFactory.selectFrom(pointDetail)
                .where(pointDetail.signUpId.isNotNull())
                .where(pointDetail.signUpId.eq(id))
                .fetch();
    }

    @Override
    public int sumAmount(Long memberId) {
        return jpaQueryFactory.select(pointDetail.amount.sum())
                .from(pointDetail)
                .where(pointDetail.membershipId.eq(memberId))
                .fetchOne();
    }

    @Override
    public List<PointDetail> getListByPage(PointDetailSearch pointDetailSearch) {
        return jpaQueryFactory.selectFrom(pointDetail)
                .where(pointDetail.membershipId.eq(pointDetailSearch.getMemberId()))
                .limit(pointDetailSearch.getSize())
                .offset(pointDetailSearch.getOffset())
                .fetch();
    }


}
