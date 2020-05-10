package org.example.rest;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.Future;

@Component
public class Sleeper {
    @Async
    public Future<Void> sleep(final float t) throws InterruptedException {
        Thread.sleep(Math.round(t * 1000));
        return new AsyncResult<>(null);
    }
}
