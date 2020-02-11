package com.ipsy.yaowei.aws;

import com.google.common.hash.Hashing;
import lombok.Builder;
import lombok.NonNull;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Builder
public class SignatureV4 {
  private static final DateTimeFormatter AMZ_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'").withZone(ZoneId.of("UTC"));
  private static final String ALGORITHM = "AWS4-HMAC-SHA256";

  @NonNull
  private String accessKey;
  @NonNull
  private String secretKey;
  private String sessionToken;
  @Builder.Default
  private Instant instant = Instant.now();
  @NonNull
  @Builder.Default
  private String uri = "/";
  @Builder.Default
  private String queryString = "";
  @NonNull
  private String host;
  @NonNull
  private Method method;
  @NonNull
  private String service;
  @Builder.Default
  private String region = "us-east-1";
  @Builder.Default
  private String payload = "";
  private Map<String, String> headers;

  private byte[] sign(byte[] key, String msg) {
    return Hashing.hmacSha256(key).hashString(msg, StandardCharsets.UTF_8).asBytes();
  }
  public String getSignature() {
    // headers
    String amzDate = "20200211T060244Z"; //AMZ_FORMATTER.format(instant);
    String dateStamp = amzDate.substring(0,8);
    if (headers == null) headers = new HashMap<>();
    headers.put("host", host);
    headers.put("x-amz-date", amzDate);
    if (sessionToken != null) {
      headers.put("x-amz-security-token",sessionToken);
    }
    String canonicalHeaders = headers.keySet().stream()
        .sorted()
        .map(it -> it + ":" + headers.get(it) + "\n")
        .collect(Collectors.joining());
    String signedHeaders = headers.keySet().stream().sorted().collect(Collectors.joining(";"));

    // payload
    String payloadHash = Hashing.sha256().hashString(payload, StandardCharsets.UTF_8).toString();

    String canonicalRequest = method.toString() + "\n" + uri + "\n" + queryString + "\n" + canonicalHeaders + "\n" + signedHeaders + "\n" + payloadHash;

    String credentialScope = dateStamp + "/" + region + "/" + service + "/" + "aws4_request";
    String stringToSign = ALGORITHM + "\n" + amzDate + "\n" + credentialScope + "\n" + Hashing.sha256().hashString(canonicalRequest, StandardCharsets.UTF_8).toString();

    byte[] signingKey = sign(sign(sign(sign(("AWS4" + secretKey).getBytes(StandardCharsets.UTF_8),dateStamp),region),service),"aws4_request");
    String signature = Hashing.hmacSha256(signingKey).hashString(stringToSign, StandardCharsets.UTF_8).toString();
    return ALGORITHM + " Credential=" + accessKey + "/" + credentialScope + ", " + "SignedHeaders=" + signedHeaders + ", Signature=" + signature;
  }

  public static void main(String argv[]) throws Exception {
    SignatureV4 signatureV4 = SignatureV4.builder()
        .host("sts.amazonaws.com")
        .method(Method.GET)
        .queryString("Action=AssumeRole&RoleArn=arn%3Aaws%3Aiam%3A%3A450096215204%3Arole%2FPowerUserAccess&RoleSessionName=rst&Version=2011-06-15")
        .service("sts")
        .secretKey("QYO8Bo+OGoa//EsiFLrMYCVKg52nN22hF9PWUXLE")
        .accessKey("ASIA5KSFUK3BC65KNSLX")
        .sessionToken("FwoGZXIvYXdzEDQaDBBwl23%2Fq7naOr%2F5FyLaAYIFBBpHM8bJKrxDrCXUTmCp%2FotL7nHJ48Fd%2BXrb7nSboAddWlm8zPkwRyGbU7txaCCoTrOsisMLFul9pdBXc57pMVfYwnMOFpAUdTyUjzN%2BAfZS42h7BRsy07%2F2qaKGcXBHPofONkZDnuei5YydwWfaHpj0rloeGEmqxTZvUIlNIY1DCVrnGIyQxDhExrRTDAdGLTTPlP1Vz%2FKH%2BQeCnp3PV7aq6LGTG8YqtHze1tUK%2B4ttyWSY%2FYXtw3dgjws%2FJMy36f3vwq0VSs%2FUHPXCKn8D6kNWqt6IMAR6KMi0%2BPEFMjIa8LI8UNSW9D4tczppodgba72XG1x2OvttzKZRypaBOcWagyHCor%2FKVibPLiE6%2F1CK9A%3D%3D")
        .build();
    System.out.println(signatureV4.getSignature());
  }

  public static enum Method {
    GET,POST,PUT,DELETE
  }
}
