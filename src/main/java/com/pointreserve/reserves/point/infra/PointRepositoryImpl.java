package com.pointreserve.reserves.point.infra;

import com.pointreserve.reserves.point.domain.Point;
import com.pointreserve.reserves.point.ui.dto.PointSearch;
import com.pointreserve.reserves.point.domain.QPoint;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class PointRepositoryImpl implements PointRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Point> getList(PointSearch pointSearch) {
        return jpaQueryFactory.selectFrom(QPoint.point)
                .where(QPoint.point.memberId.eq(pointSearch.getMemberId()))
                .limit(pointSearch.getSize())
                .offset(pointSearch.getOffset())
                .orderBy(QPoint.point.effectiveData.asc())
                .fetch();
    }
}
