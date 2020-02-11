package com.ipsy.yaowei;

import com.ipsy.yaowei.verticles.MainVerticle;
import io.vertx.core.Vertx;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Test {
  public static void main(String []argv) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(MainVerticle.class.getName());
  }
}
