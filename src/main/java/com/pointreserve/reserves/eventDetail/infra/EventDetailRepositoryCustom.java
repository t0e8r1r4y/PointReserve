package com.pointreserve.reserves.eventDetail.infra;

import com.pointreserve.reserves.eventDetail.domain.EventDetail;
import com.pointreserve.reserves.eventDetail.ui.dto.EventDetailSearch;

import java.util.List;

public interface EventDetailRepositoryCustom {
    List<EventDetail> getListWithSignIdIsNotNull(Long memberId);
    List<EventDetail> getListWithSignId(String signUpId);
    List<EventDetail> getListWithCancelId(String cancelId);
    List<EventDetail> getListNotInSignUpId(List<String> NotIn, Long memberId);
    EventDetail getOneById(String id);
    List<EventDetail> getOneByEventId(String eventId);
    List<EventDetail> getListById(String id);
    int sumAmount(Long memberId);

    List<EventDetail> getListByPage(EventDetailSearch eventDetailSearch);

}
