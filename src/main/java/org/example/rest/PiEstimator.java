package org.example.rest;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

@Component
public class PiEstimator {

    private final Random rnd = new Random();

    /**
     * Estimate Pi by sampling uniformly distributed points in the unit square. The fraction that are at most 1 from the
     * origin will roughly equal Pi / 4.
     *
     * @param n the number of uniformly distributed points to sample
     * @return an estimate of Pi
     */
    @Async
    public Future<Double> estimatePiImpl(final int n) {

        final double estimatedPi =
                IntStream
                        .generate(() -> (Math.pow(rnd.nextDouble(), 2) + Math.pow(rnd.nextDouble(), 2) < 1 ? 4 : 0))
                        .limit(n)
                        .average()
                        .orElse(Double.NaN);

        return new AsyncResult<>(estimatedPi);
    }
}
