package com.pointreserve.reserves.accumulationpoint.infra;

import com.pointreserve.reserves.accumulationpoint.domain.AccumulatedPoint;
import org.springframework.data.jpa.repository.Lock;

import javax.persistence.LockModeType;
import java.util.Optional;

public interface AccountRepositoryCustom {
    Optional<AccumulatedPoint> getByMemberId(Long memberId);
}
