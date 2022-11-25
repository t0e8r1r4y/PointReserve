package com.pointreserve.reserves.pointdetail.infra;

import com.pointreserve.reserves.pointdetail.domain.PointDetail;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PointDetailRepository extends JpaRepository<PointDetail, String>, PointDetailRepositoryCustom {
}