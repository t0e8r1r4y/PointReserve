package com.pointreserve.reserves.eventReserves.infra;

import com.pointreserve.reserves.eventReserves.domain.EventReserves;
import com.pointreserve.reserves.eventReserves.ui.dto.EventReservesSearch;

import java.util.List;

public interface EventReservesRepositoryCustom {
    List<EventReserves> getList(EventReservesSearch eventReservesSearch);

}
