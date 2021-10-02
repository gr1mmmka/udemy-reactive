package by.khmara.service;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

class FluxAndMonoSchedulerServiceTest {
    private FluxAndMonoSchedulerService fluxAndMonoSchedulerService = new FluxAndMonoSchedulerService();

    @Test
    void fluxPublishOn() {
        var names = fluxAndMonoSchedulerService.fluxPublishOn();

        StepVerifier.create(names)
                .expectNextCount(6)
                .verifyComplete();
    }

    @Test
    void fluxSubscribeOn() {
        var names = fluxAndMonoSchedulerService.fluxSubscribeOn();

        StepVerifier.create(names)
                .expectNextCount(6)
                .verifyComplete();
    }

    @Test
    void fluxParallel() {
        var names = fluxAndMonoSchedulerService.fluxParallel();

        StepVerifier.create(names)
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void fluxParallel_withFlatMap() {
        var names = fluxAndMonoSchedulerService.fluxParallel_withFlatMap();

        StepVerifier.create(names)
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void fluxParallel_withFlatMapSequential() {
        var names = fluxAndMonoSchedulerService.fluxParallel_withFlatMapSequential();

        StepVerifier.create(names)
                .expectNextCount(3)
                .verifyComplete();
    }
}