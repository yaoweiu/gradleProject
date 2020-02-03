package com.ipsy.yaowei

import spock.lang.Specification

class TestSpec extends Specification {
  def "testing"() {
    given:
    def a = 2
    when:
    def b = a * 2
    then:
    assert b == 4
  }
}
