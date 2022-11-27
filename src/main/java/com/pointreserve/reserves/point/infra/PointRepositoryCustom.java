package com.pointreserve.reserves.point.infra;

import com.pointreserve.reserves.point.domain.Point;
import com.pointreserve.reserves.point.ui.dto.PointSearch;

import java.util.List;

public interface PointRepositoryCustom {

  List<Point> getList(PointSearch pointSearch);

}
