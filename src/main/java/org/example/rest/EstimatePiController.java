package org.example.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
public class EstimatePiController {


    @Autowired
    private PiEstimator estimator;


    @GetMapping("/estimate-pi/{n}")
    public Map<String, ?> estimatePi(@PathVariable final Integer n) throws ExecutionException, InterruptedException {

        final long start = System.nanoTime();
        final double estimatedPi = estimator.estimatePiImpl(n).get();
        final double elapsed = (System.nanoTime() - start) / 1e9;

        return Map.of("estimatedPi", estimatedPi, "elapsed", elapsed);
    }
}
