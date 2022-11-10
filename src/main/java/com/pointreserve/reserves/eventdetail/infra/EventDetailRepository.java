package com.pointreserve.reserves.eventdetail.infra;

import com.pointreserve.reserves.eventdetail.domain.EventDetail;
import org.springframework.data.jpa.repository.JpaRepository;


public interface EventDetailRepository extends JpaRepository<EventDetail, String>, EventDetailRepositoryCustom {
}