package com.ipsy.yaowei.pojos;

import lombok.Data;

import java.time.*;
import java.util.Date;

@Data
public class DateTime {
  private Date  date;
  private Instant instant;
  private Duration duration;
  private LocalDate localDate;
  private LocalDateTime localDateTime;
  private ZonedDateTime zonedDateTime;
}
