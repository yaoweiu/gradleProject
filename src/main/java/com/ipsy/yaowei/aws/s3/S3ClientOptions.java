package com.ipsy.yaowei.aws.s3;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class S3ClientOptions {
  private String region;
  private String role;
}
