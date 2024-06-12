package ramyunlab_be.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {
  @Value("${cloud.aws.region.static}")
  private String s3Region;

  @Value("${cloud.aws.credentials.accessKey}")
  private String awsAccessKey;

  @Value("${cloud.aws.credentials.secretKey}")
  private String awsSecretKey;

  @Bean
  public AmazonS3Client amazonS3Client () {
    BasicAWSCredentials awsCredentials = new BasicAWSCredentials(awsAccessKey, awsSecretKey);

    return (AmazonS3Client) AmazonS3ClientBuilder
        .standard()
        .withRegion(s3Region)
        .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
        .build();
  }
}
