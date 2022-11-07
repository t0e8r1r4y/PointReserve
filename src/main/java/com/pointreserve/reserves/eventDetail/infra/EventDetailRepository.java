package com.pointreserve.reserves.eventDetail.infra;

import com.pointreserve.reserves.eventDetail.domain.EventDetail;
import org.springframework.data.jpa.repository.JpaRepository;


public interface EventDetailRepository extends JpaRepository<EventDetail, String>, EventDetailRepositoryCustom {
}