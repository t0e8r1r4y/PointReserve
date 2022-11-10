package com.pointreserve.reserves.accumulationpoint.application.service;

import com.pointreserve.reserves.accumulationpoint.domain.AccumulatedPoint;
import com.pointreserve.reserves.accumulationpoint.domain.AccumulatedPointEditor;
import com.pointreserve.reserves.accumulationpoint.exception.AccumulatedPointConfilctException;
import com.pointreserve.reserves.accumulationpoint.infra.AccumulatedPointPointRepository;
import com.pointreserve.reserves.accumulationpoint.exception.AccumulatedPointNotFoundException;
import com.pointreserve.reserves.accumulationpoint.ui.dto.AccumulatedPointCreate;
import com.pointreserve.reserves.accumulationpoint.ui.dto.AccumulatedPointEdit;
import com.pointreserve.reserves.accumulationpoint.ui.dto.AccumulatedPointResponse;
import com.pointreserve.reserves.common.bucket.TrafficPlan;
import io.github.bucket4j.Bucket;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// 금액 업데이트에 대한 내용을 증가, 감소로 처리하는게 맞다.
// 입력을 얼마를 받고 -> 계산을 해당 레이어에서

@Slf4j
@Service
@RequiredArgsConstructor
public class AccumulatedPointService {
    private final AccumulatedPointPointRepository accumulatedPointPointRepository;

    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    public Bucket resolveBucket(String apiKey) {
        return cache.computeIfAbsent(apiKey, this::newBucket);
    }

    public void clearBucket(){
        cache.clear();
    }

    private Bucket newBucket(String apiKey) {
        TrafficPlan trafficPlan = TrafficPlan.resolvePlanFromApiKey(apiKey);
        return Bucket.builder()
                .addLimit(trafficPlan.getLimit())
                .build();
    }

    @Transactional
    public AccumulatedPointResponse createAccumulatedPoint(AccumulatedPointCreate accumulatedPointCreate) {
        if( accumulatedPointPointRepository.getByMemberId(accumulatedPointCreate.getMemberId()).isPresent() ){
            throw new AccumulatedPointConfilctException();
        }
        return AccumulatedPointResponse.builder()
                .accumulatedPoint( accumulatedPointPointRepository.save( accumulatedPointCreate.toEntity() )  )
                .build();
    }

    @Transactional
    public AccumulatedPointResponse deleteAccumulatedPoint(Long memberId ) {
        AccumulatedPoint accumulatedPoint = accumulatedPointPointRepository.getByMemberId(memberId)
                .orElseThrow(() -> new AccumulatedPointNotFoundException());
        accumulatedPointPointRepository.delete(accumulatedPoint);
        return new AccumulatedPointResponse(accumulatedPoint);
    }

    @Transactional
    public AccumulatedPointResponse updateAccumulatedPoint(Long memberId, AccumulatedPointEdit accumulatedPointEdit){
        AccumulatedPoint accumulatedPoint = accumulatedPointPointRepository.getByMemberId(memberId)
                .orElseThrow(() -> new AccumulatedPointNotFoundException());

        AccumulatedPointEditor.AccumulatedPointEditorBuilder amountEditorBuilder = accumulatedPoint.toEditor();
        AccumulatedPointEditor accumulatedPointEditor = amountEditorBuilder.totalAmount(accumulatedPointEdit.getTotalAmount()).build();

        accumulatedPoint.edit(accumulatedPointEditor);
        AccumulatedPoint saveResult = accumulatedPointPointRepository.saveAndFlush(accumulatedPoint);

        return new AccumulatedPointResponse(saveResult);
    }
    @Transactional
    public AccumulatedPointResponse getAccumulatedPoint(Long memberId ){
        AccumulatedPoint accumulatedPoint = accumulatedPointPointRepository.getByMemberId(memberId)
                .orElseThrow(() -> new AccumulatedPointNotFoundException());
        return AccumulatedPointResponse.builder().accumulatedPoint(accumulatedPoint).build();
    }



}
