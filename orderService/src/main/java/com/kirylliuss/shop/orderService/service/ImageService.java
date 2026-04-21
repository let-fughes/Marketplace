package com.kirylliuss.shop.orderService.service;

import com.kirylliuss.shop.orderService.model.Item;
import com.kirylliuss.shop.orderService.repository.ItemRepository;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {

    @Value("${minio.url}")
    private String url;

    @Value("${minio.bucket}")
    private String bucket;

    private final MinioClient minioClient;
    private final ItemRepository repository;

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

    public String getImage(Long id){
        Item item = repository.findById(id).orElseThrow(() -> new RuntimeException("Something went wrong while loading image url."));
        return item.getImageUrl();
    }
}
