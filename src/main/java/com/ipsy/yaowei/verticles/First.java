package com.ipsy.yaowei.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.client.WebClient;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class First extends AbstractVerticle {
  @Override
  public void start() {
    WebClient webClient = WebClient.create(vertx);
    vertx.createHttpServer().requestHandler(req -> {
      webClient.get(80,"www.google.com","/").send(ar -> {
        if (ar.succeeded()) {
          System.out.println(ar.result().bodyAsString());
          req.response()
//              .putHeader("content-type", "text/plain")
              .end(ar.result().bodyAsString());

        }
//        log.info("{}",
//            ar.cause());
//        req.response()
//            .end("hello");
      });
    }).listen(8080);
  }
}
