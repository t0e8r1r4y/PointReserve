package com.pointreserve.reserves.common.event;

import com.pointreserve.reserves.pointdetail.ui.dto.PointDetailCreate;
import lombok.extern.slf4j.Slf4j;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
public class EventDetailCreateQueue {

  private final Queue<PointDetailCreate> queue;
  private final int queueSize;

  public EventDetailCreateQueue(int queueSize) {
    this.queue = new LinkedBlockingQueue<>(queueSize);
    this.queueSize = queueSize;
  }

  public static EventDetailCreateQueue of(int size) {
    return new EventDetailCreateQueue(size);
  }

  public boolean offer(PointDetailCreate pointDetailCreate) {
    boolean returnValue = queue.offer(pointDetailCreate);
    healthCheck();
    return returnValue;
  }

  public PointDetailCreate poll() {
    if (queue.isEmpty()) {
      throw new IllegalArgumentException("이벤트 큐에 이벤트 없음");
    }

    PointDetailCreate pointDetailCreate = queue.poll();
    healthCheck();
    return pointDetailCreate;
  }

  private int size() {
    return queue.size();
  }

  public boolean isFull() {
    return size() == queueSize;
  }

  public boolean isRemaining() {
    return size() > 0;
  }

  private void healthCheck() {
    log.info("{\"totalQueueSize\":{}, \"currentQueueSize\":{}}", queueSize, size());
  }
}
