package org.example.rest;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class Sleeper {
    @Async
    public void sleep(final float t) throws InterruptedException {
        Thread.sleep(Math.round(t * 1000));
    }
}
