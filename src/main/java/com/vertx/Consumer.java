package com.vertx;

import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.List;

public class Consumer {
    private EventBus eventBus;
    public Consumer(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public void init() {

        this.eventBus.consumer("COMMON-TOPIC" , objectMessage -> {
            objectMessage.reply(null);
            JsonObject body = (JsonObject) objectMessage.body();
            List series = (List)((JsonArray)body.getValue("series")).getList();
            System.out.println("received "+ series.size());
         /* try {
              Thread.sleep(2000);
          } catch (Exception e) {
              e.printStackTrace();
          }*/
            //objectMessage.reply(null);
        });
        System.out.println("Registered consumers");
    }
}
