package by.khmara.service;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.ParallelFlux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import javax.print.attribute.standard.SheetCollate;
import java.util.List;
import java.util.Locale;

import static by.khmara.util.CommonUtil.delay;

@Slf4j
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
        var fluxNames = getFlux(nameList)
                .subscribeOn(Schedulers.boundedElastic())
                .log();
        var fluxNames1 = getFlux(nameList1)
                .subscribeOn(Schedulers.boundedElastic())
                .log();

        return fluxNames.mergeWith(fluxNames1);
    }

    public ParallelFlux<String> fluxParallel() {
        var noOfCores = Runtime.getRuntime().availableProcessors();
        log.info("Number of Cores: {}", noOfCores);

        return Flux.fromIterable(nameList)
                .parallel()
                .runOn(Schedulers.parallel())
                .map(this::upperCase)
                .log();
    }

    public Flux<String> fluxParallel_withFlatMap() {
        return Flux.fromIterable(nameList)
                .flatMap(name -> Mono.just(name)
                        .map(this::upperCase)
                        .subscribeOn(Schedulers.parallel()))

                .log();
    }

    public Flux<String> fluxParallel_withFlatMapSequential() {
        return Flux.fromIterable(nameList)
                .flatMapSequential(name -> Mono.just(name)
                        .map(this::upperCase)
                        .subscribeOn(Schedulers.parallel()))

                .log();
    }

    private Flux<String> getFlux(List<String> nameList) {
        return Flux.fromIterable(nameList)
                .publishOn(Schedulers.parallel())
                .map(this::upperCase);
    }

    private String upperCase(String name) {
        delay(1000);
        return name.toUpperCase();
    }
}
