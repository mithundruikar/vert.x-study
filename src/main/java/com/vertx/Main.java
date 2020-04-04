package com.vertx;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.impl.VertxImpl;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * run with program arguments for publisher: publisher 3700
 * run with program arguments for publisher: consumer 3701
 */
public class Main {

    public static void main(String[] args) {
        VertxOptions vertxOptions = new VertxOptions();
        vertxOptions.setClusterHost("localhost");
        vertxOptions.setClusterPort(Integer.valueOf(args[1]));
        vertxOptions.setClustered(true);
        AtomicReference<VertxImpl> vertxAtomicReference = new AtomicReference<>();
        Vertx.clusteredVertx(vertxOptions , res -> {
            if (res.succeeded()) {
                VertxImpl vertx = (VertxImpl)res.result();
                EventBus eventBus = vertx.eventBus();
                System.out.println("We now have a clustered event bus: " + eventBus);
                new Thread( () -> {
                    if ("publisher".equals(args[0])) {
                        Publisher publisher = new Publisher(eventBus);
                        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
                        ScheduledFuture<?> hello = scheduledExecutorService.scheduleAtFixedRate(() -> {
                                try {
                                    publisher.publish("hello");
                                } catch(Exception e) {
                                    e.printStackTrace();
                        } }
                                , 1
                                , 5
                                , TimeUnit.SECONDS);

                    } else {
                        Consumer consumer = new Consumer(eventBus);
                        consumer.init();
                    }
                }).start();
            } else {
                System.out.println("Failed: " + res.cause());
            }
        });


    }
}
