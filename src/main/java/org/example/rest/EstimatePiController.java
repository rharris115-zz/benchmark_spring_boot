package org.example.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;


import java.util.Map;
import java.util.Random;
import java.util.stream.Stream;

@RestController
public class EstimatePiController {


    @GetMapping("/estimate-pi/{n}")
    public Map<String, Double> estimatePi(@PathVariable Integer n) {


        final Random rnd = new Random();


        final double piEstimate = 4d * Stream.generate(() -> {
            double x = rnd.nextDouble();
            double y = rnd.nextDouble();
            return (x * x + y * y) < 1 ? 1 : 0;
        })
                .mapToInt(Integer::intValue)
                .limit(n)
                .sum()
                / n;


        return Map.of("estimated_pi", piEstimate);
    }
}
