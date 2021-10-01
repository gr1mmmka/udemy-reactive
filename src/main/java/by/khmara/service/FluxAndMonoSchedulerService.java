package by.khmara.service;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.Locale;

import static by.khmara.util.CommonUtil.delay;

public class FluxAndMonoSchedulerService {
    static List<String> nameList = List.of("anton", "vika", "igor");
    static List<String> nameList1 = List.of("maksim", "vasya", "petr");

    public Flux<String> fluxPublishOn() {
        var fluxNames = Flux.fromIterable(nameList)
                .publishOn(Schedulers.parallel())
                .map(this::upperCase)
                .log();
        var fluxNames1 = Flux.fromIterable(nameList1)
                .publishOn(Schedulers.parallel())
                .map(this::upperCase)
                .log();

        return fluxNames.mergeWith(fluxNames1);
    }

    public Flux<String> fluxSubscribeOn() {
        var fluxNames = getFlux()
                .subscribeOn(Schedulers.boundedElastic())
                .log();
        var fluxNames1 = getFlux()
                .subscribeOn(Schedulers.boundedElastic())
                .log();

        return fluxNames.mergeWith(fluxNames1);
    }

    private Flux<String> getFlux() {
        return Flux.fromIterable(nameList)
                .publishOn(Schedulers.parallel())
                .map(this::upperCase);
    }

    private String upperCase(String name) {
        delay(1000);
        return name.toUpperCase();
    }
}
