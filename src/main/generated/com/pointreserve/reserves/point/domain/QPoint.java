package com.pointreserve.reserves.point.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPoint is a Querydsl query type for Point
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPoint extends EntityPathBase<Point> {

    private static final long serialVersionUID = -1350982778L;

    public static final QPoint point = new QPoint("point");

    public final com.pointreserve.reserves.common.domain.QBaseTimeEntity _super = new com.pointreserve.reserves.common.domain.QBaseTimeEntity(this);

    public final NumberPath<Integer> amount = createNumber("amount", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> effectiveData = _super.effectiveData;

    public final StringPath id = createString("id");

    public final NumberPath<Long> memberId = createNumber("memberId", Long.class);

    public final EnumPath<PointStatus> status = createEnum("status", PointStatus.class);

    public QPoint(String variable) {
        super(Point.class, forVariable(variable));
    }

    public QPoint(Path<? extends Point> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPoint(PathMetadata metadata) {
        super(Point.class, metadata);
    }

}

