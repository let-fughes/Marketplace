package com.kirylliuss.shop.userService.service;

import com.kirylliuss.shop.userService.config.MinIOConfig;
import com.kirylliuss.shop.userService.model.User;
import com.kirylliuss.shop.userService.repository.UserRepository;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileImageService {

    @Value("${minio.url}")
    private String url;

    @Value("${minio.bucket}")
    private String bucket;

    private MinioClient minioClient;
    private UserRepository userRepository;

    public String uploadImage(MultipartFile file){
        try{
            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(filename)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );

            return url + "/" + bucket + "/" + filename;
        } catch (Exception ex){
            throw  new RuntimeException("Something went wrong while uploading image with Minio: ", ex);
        }
    }

    public String getProfilePhoto(String login){
        User user = userRepository.findByLogin(login).orElseThrow(() -> new RuntimeException("User Not Found."));
        return user.getProfilePhotoUrl();
    }
}
