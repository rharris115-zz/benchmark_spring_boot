package org.example.rest;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

@Component
public class PiEstimator {

    @Async
    public Future<Double> estimatePiImpl(final int n) {

        final Random rnd = new Random();

        /*
         * Estimate pie from the fraction of uniformly distributed points in the unit square that are at most 1 from the
         * origin. Pi will roughly equal 4 times this fraction.
         */

        double estimatedPi = IntStream.generate(() -> {
            double x = rnd.nextDouble();
            double y = rnd.nextDouble();
            return (x * x + y * y) < 1 ? 4 : 0; // Don't need to multiply by 4 later.
        })
                .limit(n)
                .average()
                .orElse(Double.NaN);

        return new AsyncResult<>(estimatedPi);
    }
}
