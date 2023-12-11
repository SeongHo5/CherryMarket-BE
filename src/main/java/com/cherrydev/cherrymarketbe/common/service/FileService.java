package com.cherrydev.cherrymarketbe.common.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.cherrydev.cherrymarketbe.common.exception.ServiceFailedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.cherrydev.cherrymarketbe.common.exception.enums.ExceptionStatus.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

    private final AmazonS3Client objectStorageClient;

    public static final String BUCKET_NAME = "cherry-resource";
    public static final String[] SUPPORTED_IMAGE_FORMAT = {"jpg", "jpeg", "png"};
    public static final int FILE_LIMIT_MAX_COUNT = 3;
    public static final long FILE_LIMIT_MAX_SIZE = 3L * 1024 * 1024; // 3MB
    public static final String DIRECTORY_SEPARATOR = "/";

    /**
     * 단일 파일 업로드
     *
     * @param multipartFile 업로드할 파일(이미지만 가능)
     * @param dirName       업로드할 디렉토리 이름
     */
    public String uploadSingleFile(MultipartFile multipartFile, String dirName) {
        checkFileExist(multipartFile);
        checkFileFormat(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        checkFileSizeLimit(multipartFile.getSize());

        ObjectMetadata objectMetadata = new ObjectMetadata();
        multipartFile.getOriginalFilename();
        objectMetadata.setContentLength(multipartFile.getSize());
        objectMetadata.setContentType(multipartFile.getContentType());

        String fileName = dirName + DIRECTORY_SEPARATOR + multipartFile.getOriginalFilename();
        try {
            return putFileToBucket(multipartFile.getInputStream(), fileName, objectMetadata);
        } catch (IOException e) {
            throw new ServiceFailedException(FAILED_TO_UPLOAD_FILE);
        }
    }

    /**
     * 다중 파일 업로드
     *
     * @param multipartFiles 업로드할 파일(이미지만 가능)
     * @param dirName        업로드할 디렉토리 이름
     */
    public void uploadMultipleFiles(List<MultipartFile> multipartFiles, String dirName) {

        checkFileExist(multipartFiles);

        List<String> fileNames = new ArrayList<>();
        multipartFiles.stream()
                .filter(Objects::nonNull)
                .forEach(file -> {
                    checkFileSizeLimit(file.getSize());
                    checkFileFormat(Objects.requireNonNull(file.getOriginalFilename()));
                    fileNames.add(file.getOriginalFilename());
                });
        checkFileCountLimit(fileNames.size());

        ObjectMetadata objectMetadata = new ObjectMetadata();

        for (MultipartFile multipartFile : multipartFiles) {
            objectMetadata.setContentLength(multipartFile.getSize());
            objectMetadata.setContentType(MediaType.IMAGE_JPEG_VALUE);

            String fileName = dirName + DIRECTORY_SEPARATOR + multipartFile.getOriginalFilename();
            try {
                log.info("Upload Image to Object Storage : " + fileName);
                putFileToBucket(multipartFile.getInputStream(), fileName, objectMetadata);
            } catch (IOException e) {
                throw new ServiceFailedException(FAILED_TO_UPLOAD_FILE);
            }
        }
    }

    /**
     * 단일 파일 삭제
     *
     * @param url     파일 URL
     * @param dirName 삭제할 파일이 있는 디렉토리 이름
     */
    public void deleteSingleFile(String url, String dirName) {
        checkFileExist(url);
        deleteFileFromBucket(url, dirName);
    }

    /**
     * 다중 파일 삭제
     * @param urls 파일 URL 목록
     * @param dirName 삭제할 파일이 있는 디렉토리 이름
     */
    public void deleteMultipleFiles(List<String> urls, String dirName) {
        urls.forEach(url -> deleteFileFromBucket(url, dirName));
    }

    // ============== PRIVATE METHODS ==============

    /**
     * Object Storage 파일 업로드 처리
     */
    private String putFileToBucket(InputStream file, String fileName, ObjectMetadata objectMetadata) {
        objectStorageClient.putObject(
                new PutObjectRequest(BUCKET_NAME, fileName, file, objectMetadata).withCannedAcl(
                        CannedAccessControlList.PublicRead));
        return objectStorageClient.getUrl(BUCKET_NAME, fileName).toString();
    }

    /**
     * Object Storage 파일 삭제 처리
     */
    private void deleteFileFromBucket(String url, String dirName) {
        final String[] split = url.split("/");
        final String fileName = dirName + DIRECTORY_SEPARATOR + split[split.length - 1];
        DeleteObjectRequest request = new DeleteObjectRequest(BUCKET_NAME, fileName);
        log.info("Deleted Image from Object Storage : " + request);
        objectStorageClient.deleteObject(request);
    }

    // ------ VALIDATION ------ //

    /**
     * 단일 파일 업로드 시 파일 null 체크
     */
    private void checkFileExist(MultipartFile multipartFile) {
        if (multipartFile == null || multipartFile.isEmpty()) {
            throw new ServiceFailedException(NO_FILE_TO_UPLOAD);
        }
    }

    /**
     * 다중 파일 업로드 시 파일 null 체크
     */
    private void checkFileExist(List<MultipartFile> multipartFiles) {
        if (multipartFiles == null
                || multipartFiles.isEmpty()
                || multipartFiles.stream().anyMatch(file -> file == null || file.isEmpty())) {
            throw new ServiceFailedException(NO_FILE_TO_UPLOAD);
        }
    }

    /**
     * 파일 삭제 시 파일 존재 여부 체크
     */
    private void checkFileExist(String url) {
        boolean isExist = objectStorageClient.doesObjectExist(BUCKET_NAME, url);
        if (!isExist) {
            throw new ServiceFailedException(NOT_FOUND_FILE);
        }
    }

    /**
     * 단일 파일 형식(확장자) 체크
     */
    private void checkFileFormat(String fileName) {
        int index = fileName.lastIndexOf(".");
        String extension = fileName.substring(index + 1).toLowerCase();
        boolean isSupported = Arrays.asList(SUPPORTED_IMAGE_FORMAT).contains(extension);
        if (!isSupported) {
            throw new ServiceFailedException(UNSUPPORTED_FILE_FORMAT);
        }
    }

    /**
     * 1회 업로드 가능한 파일 개수 체크
     */
    private void checkFileCountLimit(int fileCounts) {
        if (fileCounts > FILE_LIMIT_MAX_COUNT) {
            throw new ServiceFailedException(TOO_MANY_FILES);
        }
    }

    /**
     * 업로드 가능한 파일 용량 체크
     */
    private void checkFileSizeLimit(long fileSize) {
        if (fileSize > FILE_LIMIT_MAX_SIZE) {
            throw new ServiceFailedException(FILE_TOO_LARGE);
        }
    }


}
