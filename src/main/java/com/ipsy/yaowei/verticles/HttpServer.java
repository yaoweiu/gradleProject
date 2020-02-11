package com.ipsy.yaowei.verticles;

import com.ipsy.pojos.event.segment.Event;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.redis.RedisClient;
import io.vertx.redis.RedisOptions;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpServer extends AbstractVerticle {
  public void start() {
    RedisClient redis = RedisClient.create(vertx, new RedisOptions());
    Router router = Router.router(vertx);
    router.route("/").method(HttpMethod.POST)
        .handler(BodyHandler.create());
    router.route("/").method(HttpMethod.POST).handler(routingContext -> {
      log.info("-- {}", routingContext.getBodyAsJson().mapTo(Event.class));
      HttpServerResponse response = routingContext.response();
      response.setChunked(true).write("ok").setStatusCode(200).end();
    });
    log.info("----");
    vertx.createHttpServer().requestHandler(router::accept).listen(8080);
  }
}
