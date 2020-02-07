package com.ipsy.yaowei.verticles;

import com.ipsy.segment.utils.SegmentCoreUtils;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.Json;

public class MainVerticle extends AbstractVerticle {
  @Override
  public void start() {
    Json.mapper = SegmentCoreUtils.OBJECT_MAPPER.copy();
    vertx.deployVerticle(HttpServer.class.getName());
  }
}
