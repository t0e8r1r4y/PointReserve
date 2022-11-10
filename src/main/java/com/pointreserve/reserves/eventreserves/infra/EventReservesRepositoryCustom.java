package com.pointreserve.reserves.eventreserves.infra;

import com.pointreserve.reserves.eventreserves.domain.EventReserves;
import com.pointreserve.reserves.eventreserves.ui.dto.EventReservesSearch;

import java.util.List;

public interface EventReservesRepositoryCustom {
    List<EventReserves> getList(EventReservesSearch eventReservesSearch);

}
