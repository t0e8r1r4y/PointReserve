package com.pointreserve.reserves.accumulationpoint.infra;

import com.pointreserve.reserves.accumulationpoint.domain.AccumulatedPoint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccumulatedPointPointRepository extends JpaRepository<AccumulatedPoint, Long>, AccountRepositoryCustom {
}
