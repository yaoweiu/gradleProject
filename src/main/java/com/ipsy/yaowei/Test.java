package com.ipsy.yaowei;

import com.github.fge.jsonschema.main.JsonSchemaFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.net.URL;

@Slf4j
public class Test {
  public static void main(String []argv) throws Exception {
    log.info("Hello world");
    JsonSchemaFactory factory = JsonSchemaFactory.byDefault();

    URL input = Test.class.getClassLoader().getResource("lifecycle_audit.json");
    factory.getJsonSchema(input.toString());
  }
}
