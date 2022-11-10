package com.pointreserve.reserves.eventreserves.infra;

import com.pointreserve.reserves.eventreserves.domain.EventReserves;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventReservesRepository extends JpaRepository<EventReserves, String>, EventReservesRepositoryCustom {
}
