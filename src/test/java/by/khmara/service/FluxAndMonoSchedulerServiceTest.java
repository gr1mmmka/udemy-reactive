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
}