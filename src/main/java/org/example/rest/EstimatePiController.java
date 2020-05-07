package org.example.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;


import java.util.Map;
import java.util.OptionalDouble;
import java.util.Random;
import java.util.stream.Stream;

@RestController
public class EstimatePiController {


    @GetMapping("/estimate-pi/{n}")
    public Map<String, ?> estimatePi(@PathVariable Integer n) {

        final long start = System.nanoTime();

        final Random rnd = new Random();

        final OptionalDouble piEstimate = Stream.generate(() -> {
            double x = rnd.nextDouble();
            double y = rnd.nextDouble();
            return (x * x + y * y) < 1 ? 4 : 0; // Don't need to multiply later.
        })
                .mapToInt(Integer::intValue)
                .limit(n)
                .average();

        final double elapsed = (System.nanoTime() - start) / 1e9;

        return Map.of("estimatedPi", piEstimate.orElse(Double.NaN), "elapsed", elapsed);
    }
}
