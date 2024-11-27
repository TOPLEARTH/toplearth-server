package com.gdsc.toplearth_server.application.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.gdsc.toplearth_server.core.exception.CustomException;
import com.gdsc.toplearth_server.core.exception.ErrorCode;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3Service {
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.plogging-poster}")
    private String bucketPloggingPoster;

    public String uploadPloggingImage(MultipartFile multipartFile, Long ploggingId) {
        String imageUrl = "";
        String fileName = createFileName(multipartFile.getOriginalFilename(), ploggingId);
        ObjectMetadata objectMetadata = new ObjectMetadata();

        try (InputStream originalInputStream = multipartFile.getInputStream()) {

            objectMetadata.setContentLength(multipartFile.getSize());
            objectMetadata.setContentType(multipartFile.getContentType());

            amazonS3Client.putObject(
                    new PutObjectRequest(
                            bucketPloggingPoster,
                            fileName,
                            originalInputStream,
                            objectMetadata
                    )
            );

            String imgUrl = amazonS3Client.getUrl(bucketPloggingPoster, fileName).toString();
            imageUrl = URLDecoder.decode(imgUrl, StandardCharsets.UTF_8);
            log.info(URLDecoder.decode(imgUrl, StandardCharsets.UTF_8));
        } catch (IOException e) {
            log.error("Error processing image for modifyInfoId: {}, fileName: {}", ploggingId, fileName, e);
        }

        return imageUrl;
    }

    // 이미지파일명 중복 방지
    private String createFileName(String fileName, Long ploggingId) {
        if (fileName.isEmpty()) {
            throw new CustomException(ErrorCode.MISSING_REQUEST_IMAGES);
        }

        // 파일 확장자 추출
        String extension = getFileExtension(fileName);
        // 파일 이름에서 확장자를 제외한 부분 추출
        String baseName = fileName.substring(0, fileName.lastIndexOf("."));
        // S3에 저장될 경로 구성: popupId 폴더 안에 원본 파일 이름으로 저장
        return ploggingId + "/" + baseName + extension;
    }

    // 파일 유효성 검사
    private String getFileExtension(String fileName) {
        ArrayList<String> fileValidate = new ArrayList<>();
        fileValidate.add(".jpg");
        fileValidate.add(".jpeg");
        fileValidate.add(".png");
        fileValidate.add(".JPG");
        fileValidate.add(".JPEG");
        fileValidate.add(".PNG");
        fileValidate.add(".tiff");
        fileValidate.add(".TIFF");
        fileValidate.add(".svg");
        fileValidate.add(".SVG");
        fileValidate.add(".webp");
        fileValidate.add(".WebP");
        fileValidate.add(".WEBP");
        fileValidate.add(".jfif");
        fileValidate.add(".JFIF");
        fileValidate.add(".HEIF");
        fileValidate.add(".heif");
        fileValidate.add(".HEIC");
        fileValidate.add(".heic");
        fileValidate.add(".heic");
        log.info("image validate : " + fileName);
        String idxFileName = fileName.substring(fileName.lastIndexOf("."));
        log.info("idx file name : " + idxFileName);
        if (!fileValidate.contains(idxFileName)) {
            throw new CustomException(ErrorCode.UNSUPPORTED_MEDIA_TYPE);
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }
}
