package com.pointreserve.reserves.common.config;

import com.pointreserve.reserves.common.event.EventDetailCreateQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventQueueInitializer {

  @Bean
  public EventDetailCreateQueue eventDetailCreateQueue() {
    return EventDetailCreateQueue.of(1_000);
  }
}
