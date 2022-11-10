package com.pointreserve.reserves.eventreserves.ui.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import static java.lang.Math.*;

@Getter
@Setter
public class EventReservesSearch {

    private static final int MAX_SIZE = 1000;
    private int page;
    private int size;

    private Long memberId;

    @Builder
    public EventReservesSearch(int page, int size, Long memberId){
        this.page = page;
        this.size = size;
        this.memberId = memberId;
    }

    public long getOffset(){
        return (long)(max(1,page)-1) * min(size,MAX_SIZE);
    }
}
