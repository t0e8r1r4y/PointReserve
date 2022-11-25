package com.pointreserve.reserves.pointdetail.infra;

import com.pointreserve.reserves.pointdetail.domain.PointDetail;
import com.pointreserve.reserves.pointdetail.ui.dto.PointDetailSearch;

import java.util.List;

public interface PointDetailRepositoryCustom {
    List<PointDetail> getListWithSignIdIsNotNull(Long memberId);
    List<PointDetail> getListWithSignId(String signUpId);
    List<PointDetail> getListWithCancelId(String cancelId);
    List<PointDetail> getListNotInSignUpId(List<String> NotIn, Long memberId);
    PointDetail getOneById(String id);
    List<PointDetail> getOneByEventId(String eventId);
    List<PointDetail> getListById(String id);
    int sumAmount(Long memberId);

    List<PointDetail> getListByPage(PointDetailSearch pointDetailSearch);

}
