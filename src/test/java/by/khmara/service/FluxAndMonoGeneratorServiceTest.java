package by.khmara.service;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

class FluxAndMonoGeneratorServiceTest {

    FluxAndMonoGeneratorService fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();

    @Test
    void testNamesFlux() {
        var namesFlux = fluxAndMonoGeneratorService.namesFlux();

        StepVerifier.create(namesFlux)
                //.expectNext("Anton", "Maksim", "Veronika")
                //.expectNextCount(3)
                .expectNext("Anton")
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    void testNameMono() {
        var nameMono = fluxAndMonoGeneratorService.nameMono();

        StepVerifier.create(nameMono)
                .expectNext("Anton")
                .verifyComplete();
    }

}