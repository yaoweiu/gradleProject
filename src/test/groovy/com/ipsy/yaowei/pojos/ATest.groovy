package com.ipsy.yaowei.pojos

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonIgnoreType
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException
import com.fasterxml.jackson.databind.module.SimpleModule
import lombok.Data
import spock.lang.Specification

import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDate
import java.time.ZoneOffset

import static com.ipsy.yaowei.Utils.*

class ATest extends Specification {
  def "object mapper"() {
    given:
    def input = getClass().classLoader.getResourceAsStream("base.json")
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
    Date date = formatter.parse("2020-01-01T09:00:00+0000")
    when:
    A a = OBJECT_MAPPER.readValue(input, A)
    then:
    assert a.base.string == "string"
    assert a.base.anInt == 1
    assert a.base.aShort == 2
    assert a.base.aLong == 3
    assert a.base.aFloat == 4.0
    assert a.base.aDouble == 5.0

    assert a.dateTime.date == date
    assert a.dateTime.duration == Duration.ofMinutes(20)
    assert a.dateTime.instant == date.toInstant()
    assert a.dateTime.localDate == new LocalDate(2020,1 ,1)
    assert a.dateTime.localDateTime.toInstant(ZoneOffset.UTC) == date.toInstant()
    assert a.dateTime.zonedDateTime.toInstant() == date.toInstant()
  }

  def "deserialize the interface Object"() {
    given:
    def json = '{ic: {name: "elaine"}}'
    when:
    OBJECT_MAPPER.readValue(json, B)
    then:
    thrown(InvalidDefinitionException)
  }

  def "deserialize the interface Object with module"() {
    given:
    def json = '{ic: {name: "elaine"}}'

    SimpleModule module = new SimpleModule()
    module.addAbstractTypeMapping(IC, RealIC)
//    module.addDeserializer(IC, new JsonDeserializer<IC>(){
//      @Override
//      IC deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
//        return null
//      }
//    })
    OBJECT_MAPPER.registerModule(module)
    when:
    B b = OBJECT_MAPPER.readValue(json, B)
    then:
    noExceptionThrown()
    assert b.ic == null
  }

  def "deserialize the interface Object with mixin annotation"() {
    given:
    def json = '{ic: {name: "elaine"}}'
    OBJECT_MAPPER.addMixIn(IC, MyMixInForIgnoreType)
    when:
    B b = OBJECT_MAPPER.readValue(json, B)
    then:
    noExceptionThrown()
    assert b.ic == null
  }
  def "deserialize the interface Object with mixin class"() {
    given:
    def json = '{ic: {name: "elaine"}, base: {string: "string"}}'
    OBJECT_MAPPER.addMixIn(B, Mixin)
    when:
    B b = OBJECT_MAPPER.readValue(json, B)
    then:
    noExceptionThrown()
    assert b.ic != null
    assert b.ic.name == "elaine"
    assert b.base.string == "string"
  }
  def "deserialize the interface Object with interface mixin class"() {
    given:
    def json = '{ic: {name: "elaine"}, base: {string: "string"}}'
    OBJECT_MAPPER.addMixIn(IC, MixInIC)
    when:
    B b = OBJECT_MAPPER.readValue(json, B)
    then:
    noExceptionThrown()
    assert b.ic != null
    assert b.ic.name == "elaine"
    assert b.base.string == "string"
  }

}
@JsonIgnoreType
class MyMixInForIgnoreType {}

@Data
class RealIC implements IC {
  String name;
}
@Data
abstract class Mixin {
  @JsonDeserialize(as=RealIC)
  private IC ic
}

@JsonDeserialize(as = RealIC)
abstract class MixInIC {}