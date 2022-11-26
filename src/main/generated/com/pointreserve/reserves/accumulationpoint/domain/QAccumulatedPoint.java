package com.pointreserve.reserves.accumulationpoint.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAccumulatedPoint is a Querydsl query type for AccumulatedPoint
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAccumulatedPoint extends EntityPathBase<AccumulatedPoint> {

    private static final long serialVersionUID = 1175508755L;

    public static final QAccumulatedPoint accumulatedPoint = new QAccumulatedPoint("accumulatedPoint");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> memberId = createNumber("memberId", Long.class);

    public final NumberPath<Integer> totalAmount = createNumber("totalAmount", Integer.class);

    public QAccumulatedPoint(String variable) {
        super(AccumulatedPoint.class, forVariable(variable));
    }

    public QAccumulatedPoint(Path<? extends AccumulatedPoint> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAccumulatedPoint(PathMetadata metadata) {
        super(AccumulatedPoint.class, metadata);
    }

}

