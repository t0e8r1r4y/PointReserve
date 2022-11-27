package com.pointreserve.reserves.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.ThreadPoolExecutor;

@EnableAsync
@Configuration
public class ThreadPoolConfig {

  @Bean
  public ThreadPoolTaskScheduler taskScheduler() {
    ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
    taskScheduler.setPoolSize(Runtime.getRuntime().availableProcessors());
    taskScheduler.setThreadNamePrefix("Scheduler-Thread");
    taskScheduler.initialize();
    return taskScheduler;
  }

  @Bean
  public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
    ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
    taskExecutor.setThreadNamePrefix("Async-Thread-");
    taskExecutor.setCorePoolSize(
        Runtime.getRuntime().availableProcessors()); // ThreadTaskScheduler와 동일 이유
    taskExecutor.setMaxPoolSize(100);
    taskExecutor.setQueueCapacity(1_000);
    taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
    taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
    taskExecutor.setAllowCoreThreadTimeOut(true);
    taskExecutor.initialize();
    return taskExecutor;
  }
}
