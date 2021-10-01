package other;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
public class BackPressureTest {

    @Test
    void testBackPressure() {
        Flux<Integer> numbers = Flux.range(1, 100).log();

        numbers
                .subscribe(new BaseSubscriber<Integer>() {
                    @Override
                    protected void hookOnSubscribe(Subscription subscription) {
                        request(2);
                    }

                    @Override
                    protected void hookOnNext(Integer value) {
                        log.info("Number is {}", value);
                        if (value == 2)
                            cancel();
                    }

                    @Override
                    protected void hookOnComplete() {

                    }

                    @Override
                    protected void hookOnError(Throwable throwable) {

                    }
                });
    }

    @Test
    void testBackPressure_1() throws InterruptedException {
        Flux<Integer> numbers = Flux.range(1, 100).log();

        CountDownLatch latch = new CountDownLatch(1);
        numbers
                .subscribe(new BaseSubscriber<Integer>() {
                    @Override
                    protected void hookOnSubscribe(Subscription subscription) {
                        request(2);
                    }

                    @Override
                    protected void hookOnNext(Integer value) {
                        log.info("Number is {}", value);
                        if (value % 2==0 || value < 50)
                            request(2);
                        else
                            cancel();
                    }

                    @Override
                    protected void hookOnComplete() {

                    }

                    @Override
                    protected void hookOnError(Throwable throwable) {

                    }

                    @Override
                    protected void hookOnCancel() {
                        latch.countDown();
                    }
                });

        assertTrue(latch.await(5, TimeUnit.SECONDS));
    }

    @Test
    void testBackPressure_buffer() throws InterruptedException {
        Flux<Integer> numbers = Flux.range(1, 100).log();

        CountDownLatch latch = new CountDownLatch(1);
        numbers
                .onBackpressureBuffer(10, i -> {
                    log.info("Last element in buffer is {}", i);
                })
                .subscribe(new BaseSubscriber<Integer>() {
                    @Override
                    protected void hookOnSubscribe(Subscription subscription) {
                        request(1);
                    }

                    @Override
                    protected void hookOnNext(Integer value) {
                        log.info("Number is {}", value);
                        if (value < 50)
                            request(1);
                        else
                            hookOnCancel();
                    }

                    @Override
                    protected void hookOnComplete() {

                    }

                    @Override
                    protected void hookOnError(Throwable throwable) {

                    }

                    @Override
                    protected void hookOnCancel() {
                        latch.countDown();
                    }
                });

        assertTrue(latch.await(5, TimeUnit.SECONDS));
    }
}
