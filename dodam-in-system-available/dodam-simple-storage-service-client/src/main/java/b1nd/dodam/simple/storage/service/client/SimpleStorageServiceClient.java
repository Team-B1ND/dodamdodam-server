package b1nd.dodam.simple.storage.service.client;

import b1nd.dodam.core.exception.global.InternalServerException;
import b1nd.dodam.simple.storage.service.client.properties.CloudProperties;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SimpleStorageServiceClient {

    private final AmazonS3Client amazonS3Client;
    private final CloudProperties cloudProperties;

    public String uploadFile(MultipartFile multipartFile) {
        String bucket = cloudProperties.storage().getBucket();
            String originalFilename = bucket + "/" + UUID.randomUUID() + multipartFile.getOriginalFilename();

            ObjectMetadata metadata = createObjectMetadata(multipartFile);

        try (InputStream inputStream = multipartFile.getInputStream()){
            amazonS3Client.putObject(new PutObjectRequest(bucket, originalFilename, inputStream, metadata));

            return amazonS3Client.getUrl(bucket, originalFilename).toString();
        } catch (Exception e) {
            throw new InternalServerException();
        }
    }

    private ObjectMetadata createObjectMetadata(MultipartFile multipartFile) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());
        return metadata;
    }

    public void delete(String path) {
        String bucket = cloudProperties.storage().getBucket();
        amazonS3Client.deleteObject(bucket, path);
    }

}
