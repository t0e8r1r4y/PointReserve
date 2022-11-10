package com.pointreserve.reserves.accumulationpoint.infra;

import com.pointreserve.reserves.accumulationpoint.domain.AccumulatedPoint;

import java.util.Optional;

public interface AccountRepositoryCustom {
    Optional<AccumulatedPoint> getByMemberId(Long memberId);
}
