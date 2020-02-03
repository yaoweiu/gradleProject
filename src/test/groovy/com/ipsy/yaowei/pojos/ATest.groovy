package com.ipsy.yaowei.pojos

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
}
