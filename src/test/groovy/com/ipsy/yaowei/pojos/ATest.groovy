package com.ipsy.yaowei.pojos

import spock.lang.Specification
import static com.ipsy.yaowei.Utils.*

class ATest extends Specification {
  def "object mapper"() {
    given:
    def input = getClass().classLoader.getResourceAsStream("base.json")
    when:
    A a = OBJECT_MAPPER.readValue(input, A)
    then:
    assert a.base.string == "string"
    assert a.base.anInt == 1
    assert a.base.aShort == 2
    assert a.base.aLong == 3
    assert a.base.aFloat == 4.0
    assert a.base.aDouble == 5.0

    assert a.dateTime.date == new Date("2020-01-01T09:00:00.000Z")
  }
}
