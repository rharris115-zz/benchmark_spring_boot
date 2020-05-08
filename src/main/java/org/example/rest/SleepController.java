package org.example.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class SleepController {
    @GetMapping("/sleep/{t}")
    public Map<String, ?> sleep(@PathVariable final Float t) throws InterruptedException {
        final long start = System.nanoTime();
        Thread.sleep(Math.round(t * 1000));
        final double elapsed = (System.nanoTime() - start) / 1e9;
        return Map.of("slept", t, "elapsed", elapsed);
    }
}
