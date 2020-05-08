package org.example.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class SleepController {
    @Autowired
    private Sleeper sleeper;

    @GetMapping("/sleep/{t}")
    public Map<String, ?> sleep(@PathVariable final Float t) throws InterruptedException {
        final long start = System.nanoTime();
        sleeper.sleep(t);
        final double elapsed = (System.nanoTime() - start) / 1e9;
        return Map.of("slept", t, "elapsed", elapsed);
    }
}
