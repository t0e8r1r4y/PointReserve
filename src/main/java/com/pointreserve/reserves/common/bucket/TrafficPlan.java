package com.pointreserve.reserves.common.bucket;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Refill;

import java.time.Duration;

public enum TrafficPlan {

    ONECOUNT {
        public Bandwidth getLimit() {
            return Bandwidth.classic(1, Refill.intervally(1, Duration.ofMinutes(1)));
        }
    },

    THREECOUNT {
        public Bandwidth getLimit() {
            return Bandwidth.classic(3, Refill.intervally(3, Duration.ofSeconds(30)));
        }
    },

    FIVECOUNT {
        public Bandwidth getLimit() {
            return Bandwidth.classic(5, Refill.intervally(5, Duration.ofSeconds(30)));
        }
    },

    TENCOUNT {
        public Bandwidth getLimit() {
            return Bandwidth.classic(10, Refill.intervally(10, Duration.ofSeconds(30)));
        }
    };

    public abstract Bandwidth getLimit();

}
