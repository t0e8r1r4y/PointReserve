package com.pointreserve.reserves.pointdetail.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPointDetail is a Querydsl query type for PointDetail
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPointDetail extends EntityPathBase<PointDetail> {

    private static final long serialVersionUID = -1630662138L;

    public static final QPointDetail pointDetail = new QPointDetail("pointDetail");

    public final NumberPath<Integer> amount = createNumber("amount", Integer.class);

    public final StringPath cancelId = createString("cancelId");

    public final DateTimePath<java.time.LocalDateTime> effectiveData = createDateTime("effectiveData", java.time.LocalDateTime.class);

    public final StringPath eventId = createString("eventId");

    public final DateTimePath<java.time.LocalDateTime> expiryDate = createDateTime("expiryDate", java.time.LocalDateTime.class);

    public final StringPath id = createString("id");

    public final NumberPath<Long> membershipId = createNumber("membershipId", Long.class);

    public final StringPath signUpId = createString("signUpId");

    public final EnumPath<com.pointreserve.reserves.point.domain.PointStatus> status = createEnum("status", com.pointreserve.reserves.point.domain.PointStatus.class);

    public QPointDetail(String variable) {
        super(PointDetail.class, forVariable(variable));
    }

    public QPointDetail(Path<? extends PointDetail> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPointDetail(PathMetadata metadata) {
        super(PointDetail.class, metadata);
    }

}

