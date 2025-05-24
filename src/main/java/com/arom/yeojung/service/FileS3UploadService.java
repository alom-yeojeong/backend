package com.arom.yeojung.service;


import com.arom.yeojung.object.File;
import com.arom.yeojung.repository.FileRepository;
import com.arom.yeojung.util.exception.CustomException;
import com.arom.yeojung.util.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileS3UploadService {
  private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; //10MB 최대 파일 사이즈
  private final String bucketName = "yeojung-bucket";
  private final S3Client s3Client;  // S3Config에서 주입
  private final FileService fileService;

  public File uploadAndSaveFile(MultipartFile file) {
    if (file.getSize() > MAX_FILE_SIZE) {
      throw new CustomException(ErrorCode.FILE_SIZE_EXCEED);
    }

    String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();
    String fileUrl = "https://" + bucketName + ".s3.ap-northeast-2.amazonaws.com/" + fileName;

    try (InputStream inputStream = file.getInputStream()) { //inputStream 사용
      s3Client.putObject(PutObjectRequest.builder()
              .bucket(bucketName)
              .key(fileName)
              .contentType(file.getContentType())
              .build(),
          RequestBody.fromInputStream(inputStream, file.getSize())); //메모리 사용량 최적화
      return fileService.save(fileName, fileUrl);
    } catch (IOException e) {
      throw new CustomException(ErrorCode.FILE_UPLOAD_FAILED);
    }
  }

  // S3 파일 삭제
  public void deleteFile(String fileUrl) {
    String fileName = extractFileNameFromUrl(fileUrl);

    // 파일이 존재하는지 확인 후 삭제
    if (doesFileExist(fileName)) {
      s3Client.deleteObject(DeleteObjectRequest.builder()
          .bucket(bucketName)
          .key(fileName)
          .build());
      log.info("파일 삭제 완료: {}", fileUrl);
    } else {
      log.warn("파일이 존재하지 않음: {}", fileUrl);
    }
  }

  // S3 파일 존재 여부 확인
  public boolean doesFileExist(String fileUrl) {
    String fileName = extractFileNameFromUrl(fileUrl);
    try {
      s3Client.headObject(HeadObjectRequest.builder()
          .bucket(bucketName)
          .key(fileName)
          .build());
      return true;
    } catch (NoSuchKeyException e) {
      return false; // 파일이 존재하지 않음
    }
  }

  // 파일 URL에서 S3 경로 추출
  private String extractFileNameFromUrl(String fileUrl) {
    return fileUrl.replace("https://" + bucketName + ".s3.ap-northeast-2.amazonaws.com/", "");
  }
}
