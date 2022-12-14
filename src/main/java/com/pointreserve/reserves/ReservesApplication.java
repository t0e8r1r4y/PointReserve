package com.pointreserve.reserves;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
public class ReservesApplication {

  public static void main(String[] args) {
    System.setProperty("spring.profiles.default", "local");
    SpringApplication.run(
        ReservesApplication.class, args);
  }

}
