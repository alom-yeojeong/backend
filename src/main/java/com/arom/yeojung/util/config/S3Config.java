package com.arom.yeojung.util.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.amazonaws.*;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class S3Config {

  // OS(혹은 컨테이너) 환경 변수 AWS_ACCESS_KEY_ID 값을 주입
  @Value("${AWS_ACCESS_KEY_ID}")
  private String accessKey;

  // OS(혹은 컨테이너) 환경 변수 AWS_SECRET_ACCESS_KEY 값을 주입
  @Value("${AWS_SECRET_ACCESS_KEY}")
  private String secretKey;

  @Bean
  public S3Client s3Client() {
    return S3Client.builder()
        .region(Region.AP_NORTHEAST_2) // 서울 리전
        .credentialsProvider(StaticCredentialsProvider.create(
            AwsBasicCredentials.create(accessKey, secretKey)
        ))
        .build();
  }
}
