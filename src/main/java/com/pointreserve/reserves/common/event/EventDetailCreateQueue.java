package com.pointreserve.reserves.common.event;

import com.pointreserve.reserves.eventDetail.ui.dto.EventDetailCreate;
import lombok.extern.slf4j.Slf4j;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
public class EventDetailCreateQueue {
    private final Queue<EventDetailCreate> queue;
    private final int queueSize;

    public EventDetailCreateQueue(int queueSize) {
        this.queue = new LinkedBlockingQueue<>(queueSize);
        this.queueSize = queueSize;
    }

    public static EventDetailCreateQueue of(int size){
        return new EventDetailCreateQueue(size);
    }

    public boolean offer(EventDetailCreate eventDetailCreate) {
        boolean returnValue = queue.offer(eventDetailCreate);
        healthCheck();
        return returnValue;
    }

    public EventDetailCreate poll(){
        if(queue.size() <= 0) {
            throw new IllegalArgumentException("이벤트 큐에 이벤트 없음");
        }

        EventDetailCreate eventDetailCreate = queue.poll();
        healthCheck();
        return eventDetailCreate;
    }

    private int size(){
        return queue.size();
    }

    public boolean isFull(){
        return size() == queueSize;
    }

    public boolean isRemaining(){
        return size() > 0;
    }

    private void healthCheck(){
        log.info("{\"totalQueueSize\":{}, \"currentQueueSize\":{}}", queueSize, size());
    }
}
