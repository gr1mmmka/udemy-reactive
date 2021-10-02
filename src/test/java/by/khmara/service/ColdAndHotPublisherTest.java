package by.khmara.service;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

import java.time.Duration;

import static by.khmara.util.CommonUtil.delay;

public class ColdAndHotPublisherTest {

    @Test
    void coldPublisherTest() {
        var flux = Flux.range(1, 10);

        flux.subscribe(number -> System.out.println("Subscriber 1: " + number));
        flux.subscribe(number -> System.out.println("Subscriber 2: " + number));

    }

    @Test
    void hotPublisherTest() {
        var flux = Flux.range(1, 10)
                .delayElements(Duration.ofSeconds(1));

        ConnectableFlux<Integer> connectableFlux = flux.publish();

        connectableFlux.connect(number -> System.out.println("Subscriber 1: " + number));
        delay(4000);
        connectableFlux.connect(number -> System.out.println("Subscriber 2: " + number));
        delay(10000);

    }

    @Test
    void hotPublisherAutoConnectTest() {
        var flux = Flux.range(1, 10)
                .delayElements(Duration.ofSeconds(1));

        var hotSource = flux.publish().autoConnect(2);

        hotSource.subscribe(number -> System.out.println("Subscriber 1: " + number));
        delay(4000);
        hotSource.subscribe(number -> System.out.println("Subscriber 2: " + number));
        System.out.println("Two subscribes connected");
        delay(2000);
        hotSource.subscribe(number -> System.out.println("Subscriber 3: " + number));
        delay(10000);

    }
}
