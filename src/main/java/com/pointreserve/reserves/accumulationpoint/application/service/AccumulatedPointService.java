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
import com.pointreserve.reserves.eventReserves.domain.ReservesStatus;
import io.github.bucket4j.Bucket;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.pointreserve.reserves.eventReserves.domain.ReservesStatus.SAVEUP;


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
                .orElseThrow(AccumulatedPointNotFoundException::new);
        accumulatedPointPointRepository.delete(accumulatedPoint);
        return new AccumulatedPointResponse(accumulatedPoint);
    }

    @Transactional
    public AccumulatedPointResponse getAccumulatedPoint(Long memberId ){
        AccumulatedPoint accumulatedPoint = accumulatedPointPointRepository.getByMemberId(memberId)
                .orElseThrow(AccumulatedPointNotFoundException::new);
        return AccumulatedPointResponse.builder().accumulatedPoint(accumulatedPoint).build();
    }

    @Transactional
    public AccumulatedPointResponse updateAccumulatedPoint(AccumulatedPoint accumulatedPoint){
        AccumulatedPoint saveResult = accumulatedPointPointRepository.saveAndFlush(accumulatedPoint);
        return new AccumulatedPointResponse(saveResult);
    }


    @Transactional
    public AccumulatedPointResponse calcPointAndUpdate(Long memberId, int amount, ReservesStatus s){
        AccumulatedPoint accumulatedPoint = accumulatedPointPointRepository.findByMemberId(memberId)
                .orElseThrow(AccumulatedPointNotFoundException::new);

        AccumulatedPointEdit accumulatedPointEdit = AccumulatedPointEdit.builder()
                .totalAmount(calUpdateAmount( s, amount, accumulatedPoint.getTotalAmount() ))
                .build();

        accumulatedPointEdit.isValid();

        AccumulatedPointEditor.AccumulatedPointEditorBuilder amountEditorBuilder = accumulatedPoint.toEditor();
        AccumulatedPointEditor accumulatedPointEditor = amountEditorBuilder.totalAmount(accumulatedPointEdit.getTotalAmount()).build();

        accumulatedPoint.edit(accumulatedPointEditor);

        return updateAccumulatedPoint(accumulatedPoint);
    }

    private int calUpdateAmount( ReservesStatus s, int amout, int beforeTotalAmount) {
        return ( (s == SAVEUP) ? amout : amout*(-1) ) + beforeTotalAmount;
    }

}
