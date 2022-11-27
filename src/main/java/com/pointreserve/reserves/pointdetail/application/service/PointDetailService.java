package com.pointreserve.reserves.pointdetail.application.service;

import com.pointreserve.reserves.pointdetail.domain.PointDetail;
import com.pointreserve.reserves.pointdetail.exception.PointDetailNotFoundException;
import com.pointreserve.reserves.pointdetail.infra.PointDetailRepository;
import com.pointreserve.reserves.pointdetail.ui.dto.PointDetailCreate;
import com.pointreserve.reserves.pointdetail.ui.dto.PointDetailResponse;
import com.pointreserve.reserves.pointdetail.ui.dto.PointDetailSearch;
import com.pointreserve.reserves.point.domain.PointStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PointDetailService {

    private final PointDetailRepository pointDetailRepository;

    @Transactional(readOnly = true)
    public List<PointDetailResponse> getEventDetailList(PointDetailSearch pointDetailSearch) {
        List<PointDetailResponse> responseList = pointDetailRepository.getListByPage(pointDetailSearch)
                .stream()
                .map(PointDetailResponse::new)
                .collect(Collectors.toList());

        if (responseList.isEmpty()) {
            throw new PointDetailNotFoundException();
        }

        return responseList;
    }


    @Transactional
    public String saveUpReserves( PointDetailCreate detailCreate ) {
        PointDetail response = pointDetailRepository.save(detailCreate.toEntity());
        PointDetailResponse pointDetailResponse = new PointDetailResponse(response);
        return pointDetailResponse.getId();
    }

    @Transactional
    public List<String> cancleSavedUpBefore( String eventId, PointDetailCreate nowEvent ) {
        List<String> idList = new ArrayList<>();

        List<PointDetail> saveHistory = pointDetailRepository.getOneByEventId(eventId);

        if(saveHistory.isEmpty()) return idList; // 적립을 취소할 대상이 없음

        List<PointDetail> checkList = new ArrayList<>(checkDetailForCancelSaveUp(saveHistory.get(0))); // 무조건 1개

        if(checkList.isEmpty()) return idList; // 적립을 취소 할 수 없음

        for( PointDetail e : checkList ){
            PointDetail response = pointDetailRepository.save(
                    PointDetailCreate.builder()
                        .membershipId(nowEvent.getMembershipId())
                        .amount(e.getAmount()*(-1))
                        .eventId(nowEvent.getEventId())
                        .status(PointStatus.CANCLE_SAVEUP)
                        .signUpId(e.getId())
                        .cancelId(null)
                        .effectiveData(e.getEffectiveData())
                        .expiryDate(e.getExpiryDate())
                    .build().toEntity() );
            idList.add((response.getId()));
        }
        return idList;
    }

    private Stack<PointDetail> checkDetailForCancelSaveUp(PointDetail pointDetail) {
        Stack<PointDetail> checkList = new Stack<>();
        checkList.push(pointDetail);

        while(!checkList.isEmpty()) {
            PointDetail e = checkList.peek();
            List<PointDetail> redeemHistory = checkRedeemHistory(e);
            if(redeemHistory.isEmpty()) {
                break; // 사용이력 없음. 취소가능
            }

            checkList.pop();
            List<PointDetail> cancelHistory = new ArrayList<>();
            Iterator<PointDetail> iter = redeemHistory.iterator();
            while(iter.hasNext()){
                PointDetail ed = iter.next();
                cancelHistory.addAll(chcckCancelHistory(ed));
            }

            if(!cancelHistory.isEmpty()){
                checkList.addAll(cancelHistory);
            }
        }
        return checkList;
    }

    @Transactional(readOnly = true)
    private List<PointDetail> checkRedeemHistory(PointDetail saveUpHistory ){
        return pointDetailRepository.getListWithSignId(saveUpHistory.getId());
    }

    @Transactional(readOnly = true)
    private List<PointDetail> chcckCancelHistory(PointDetail redeemHistory ){
        return pointDetailRepository.getListWithCancelId(redeemHistory.getId());
    }



    @Transactional
    public List<String> redeemReserves( PointDetailCreate nowEvent ) {
        int nowRedeemAmount = nowEvent.getAmount(); // 저장해야 될 금액
        List<String> idList = new ArrayList<>();

        List<PointDetail> detailListWithSignUpIdNotNull = pointDetailRepository.getListWithSignIdIsNotNull(nowEvent.getMembershipId()); // 전체에서 연관 ID가 null이 아닌 list 조회 -> 한번도 사용하지 않은 경우 empty일 수 있음
        List<String> notInCondition = detailListWithSignUpIdNotNull
                .stream()
                .map(eventDetail -> (eventDetail.getSignUpId()))
                .collect(Collectors.toList()); // Id 추출
        List<PointDetail> detailListNotInSignUpId = pointDetailRepository.getListNotInSignUpId(notInCondition, nowEvent.getMembershipId()); // 해당 리스트가 포함되지 않은 상세 ID list 중 구분이 적립인 것들

        List<PointDetailCreate> createList = new ArrayList<>(); // 실제 DB에 쌓아야되는 항목

        String needCheckingId = null; // 사용 잔액이 남아있을 수 있는 항목

        if(!detailListWithSignUpIdNotNull.isEmpty()){
            needCheckingId = detailListWithSignUpIdNotNull.get(detailListWithSignUpIdNotNull.size()-1).getSignUpId();
        }

        if(needCheckingId != null){ // 사용 잔액이 남아있는지 확인
            PointDetail saveHistory = pointDetailRepository.getOneById(needCheckingId);
            List<PointDetail> redeemHistory = pointDetailRepository.getListById(needCheckingId);

            if(saveHistory == null) return idList;
            if(redeemHistory.isEmpty()) return idList;

            int saveHistoryAmount = saveHistory.getAmount();
            int redeemHistoryAmount = 0;
            for(PointDetail ed : redeemHistory) {
                redeemHistoryAmount += ed.getAmount() * (-1);
            }

            if(saveHistoryAmount != redeemHistoryAmount) { // 남아 있는 잔액이 있다면
                int tmp = 0;
                int remainAmount = saveHistoryAmount - redeemHistoryAmount;
                if( remainAmount >=  nowRedeemAmount ) {
                    tmp = nowRedeemAmount;
                    nowRedeemAmount = 0; // nowRedeemAmount만 소진하고 끝
                } else if( remainAmount <  nowRedeemAmount ) {
                    tmp = remainAmount;
                    nowRedeemAmount -= remainAmount; // remainAmount를 모두 소진
                }
                // 생성 해야 될 정보 저장
                createList.add(PointDetailCreate.builder()
                        .membershipId(nowEvent.getMembershipId())
                        .status(PointStatus.REDEEM)
                        .amount(tmp*(-1))
                        .eventId(nowEvent.getEventId())
                        .signUpId(saveHistory.getId())
                        .cancelId(null)
                        .effectiveData(nowEvent.getEffectiveData())
                        .expiryDate(nowEvent.getEffectiveData().plusYears(1))
                        .build());
            }
        }

        // 남아있는 잔액 연산 끝. 잔액 연산이 끝나고 현재 더 사용해야 될 금액이 있으면 새로운 detail에서 사용
        if(nowRedeemAmount != 0) {
            for( PointDetail e : detailListNotInSignUpId ){
                int tmp = 0;
                if(nowRedeemAmount >= e.getAmount()) {
                    tmp = e.getAmount(); // e.getAmount 사용한것으로 저장
                    nowRedeemAmount -= e.getAmount();
                } else if(nowRedeemAmount < e.getAmount()) {
                    tmp = nowRedeemAmount; // 여기서는 nowRedeemAmount만 사용한것으로 저장
                    nowRedeemAmount = 0;
                }

                createList.add(PointDetailCreate.builder()
                        .membershipId(nowEvent.getMembershipId())
                        .status(PointStatus.REDEEM)
                        .amount(tmp*(-1))
                        .eventId(nowEvent.getEventId())
                        .signUpId(e.getId())
                        .cancelId(null)
                        .effectiveData(nowEvent.getEffectiveData())
                        .expiryDate(nowEvent.getEffectiveData().plusYears(1))
                        .build());
                if(nowRedeemAmount == 0) {
                    break;
                }
            }
        }

        for( PointDetailCreate create : createList ) {
            idList.add(pointDetailRepository.save(create.toEntity()).getId());
        }

        return idList;
    }


    @Transactional
    public List<String> cancelRedeemedBefore( String eventId, PointDetailCreate nowEvent ) {

        List<String> idList = new ArrayList<>();
        List<PointDetail> pointDetailList = pointDetailRepository.getOneByEventId(eventId);

        if(pointDetailList.isEmpty()) return idList;

        for( PointDetail e : pointDetailList) {
            PointDetail newPointDetail = pointDetailRepository.save( PointDetail.builder()
                    .membershipId(nowEvent.getMembershipId()) // 새로운 이벤트
                    .status(PointStatus.CANCLE_REDEEM)     // 사용 취소는 다시 적립
                    .amount(e.getAmount() * (-1))             // 이전 사용 금액을 다시 원복
                    .eventId(nowEvent.getEventId())           // 이벤트 ID는 이번에 발생한 이벤트
                    .cancelId(e.getId())                      // 취소 ID는 이전 ID
                    .signUpId(null)                           // 신규 적립은 signUp null
                    .effectiveData(e.getEffectiveData())      // 이전 유효시간
                    .expiryDate(e.getExpiryDate())            // 이전 만료시간
                    .build() );
            idList.add(newPointDetail.getId());
        }
        return idList;
    }
}
