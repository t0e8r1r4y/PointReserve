package com.pointreserve.reserves.point.infra;

import com.pointreserve.reserves.point.domain.Point;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointRepository extends JpaRepository<Point, String>, PointRepositoryCustom {
}
