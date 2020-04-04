package com.vertx;

import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;

import java.math.BigDecimal;
import java.util.*;

public class Publisher {
    public static final int MESSAGE_POINTS = 500000;
    public static final int MESSAGE_BATCH_SIZE = 1;

    private EventBus eventBus;
    public Publisher(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public void publish(Object toPublish) {
        for (int i = 0 ; i < MESSAGE_BATCH_SIZE ; i ++) {
            List<BigDecimal> series = new LinkedList<>();
            for(int j = 0; j < MESSAGE_POINTS; j++) {
                series.add(BigDecimal.valueOf(10000*j));
            }
            JsonObject jsonObject = new JsonObject();
            jsonObject.put("series", series);
            DeliveryOptions deliveryOptions = new DeliveryOptions();
            deliveryOptions.setSendTimeout(2000);
            this.eventBus.send("COMMON-TOPIC",  jsonObject, deliveryOptions, messageAsyncResult -> {
                if(messageAsyncResult.succeeded()) {
                    System.out.println("Message consumed");
                }
            });
            //this.eventBus.publish("COMMON-TOPIC",  jsonObject);

        }
        System.out.println("Published");
    }
}
