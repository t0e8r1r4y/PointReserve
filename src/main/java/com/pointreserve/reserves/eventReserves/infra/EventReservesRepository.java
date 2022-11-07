package com.pointreserve.reserves.eventReserves.infra;

import com.pointreserve.reserves.eventReserves.domain.EventReserves;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventReservesRepository extends JpaRepository<EventReserves, String>, EventReservesRepositoryCustom {
}
