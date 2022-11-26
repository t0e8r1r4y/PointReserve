package com.pointreserve.reserves.accumulationpoint.application.service;

import com.pointreserve.reserves.accumulationpoint.domain.AccumulatedPoint;
import com.pointreserve.reserves.accumulationpoint.domain.AccumulatedPointEditor;
import com.pointreserve.reserves.accumulationpoint.exception.AccumulatedPointConfilctException;
import com.pointreserve.reserves.accumulationpoint.infra.AccumulatedPointPointRepository;
import com.pointreserve.reserves.accumulationpoint.exception.AccumulatedPointNotFoundException;
import com.pointreserve.reserves.accumulationpoint.ui.dto.AccumulatedPointCreate;
import com.pointreserve.reserves.accumulationpoint.ui.dto.AccumulatedPointEdit;
import com.pointreserve.reserves.accumulationpoint.ui.dto.AccumulatedPointResponse;
import com.pointreserve.reserves.point.domain.PointStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.pointreserve.reserves.point.domain.PointStatus.REDEEM;


@Slf4j
@Service
@RequiredArgsConstructor
public class AccumulatedPointService {
    private final AccumulatedPointPointRepository accumulatedPointPointRepository;

    public AccumulatedPointResponse createAccumulatedPoint(AccumulatedPointCreate accumulatedPointCreate) {
        if( accumulatedPointPointRepository.getByMemberId(accumulatedPointCreate.getMemberId()).isPresent() ){
            throw new AccumulatedPointConfilctException();
        }
        return AccumulatedPointResponse.builder()
                .accumulatedPoint( accumulatedPointPointRepository.save( accumulatedPointCreate.toEntity() )  )
                .build();
    }

    public AccumulatedPointResponse deleteAccumulatedPoint(Long memberId ) {
        AccumulatedPoint accumulatedPoint = accumulatedPointPointRepository.getByMemberId(memberId)
                .orElseThrow(AccumulatedPointNotFoundException::new);
        accumulatedPointPointRepository.delete(accumulatedPoint);
        return new AccumulatedPointResponse(accumulatedPoint);
    }

    @Transactional(readOnly = true)
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
    public AccumulatedPointResponse calcPointAndUpdate(Long memberId, int amount, PointStatus s){
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


    private int calUpdateAmount(PointStatus s, int amout, int beforeTotalAmount) {
        return ( (s != REDEEM) ? amout : amout*(-1) ) + beforeTotalAmount;
    }

}