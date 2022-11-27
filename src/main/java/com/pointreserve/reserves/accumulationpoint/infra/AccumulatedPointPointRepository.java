package com.pointreserve.reserves.accumulationpoint.infra;

import com.pointreserve.reserves.accumulationpoint.domain.AccumulatedPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import javax.persistence.LockModeType;
import java.util.Optional;

public interface AccumulatedPointPointRepository extends JpaRepository<AccumulatedPoint, Long>,
    AccountRepositoryCustom {

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  Optional<AccumulatedPoint> findByMemberId(Long memberId);
}
