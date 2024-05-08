package b1nd.dodaminfra.cloud;

import b1nd.dodamcore.upload.application.UploadService;
import b1nd.dodaminfra.cloud.exception.CloudException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class StorageUploadExecutor implements UploadService {

    private final AmazonS3Client amazonS3Client;
    private final CloudProperties cloudProperties;

    public String uploadFile(MultipartFile multipartFile) {
        String bucket = cloudProperties.storage().getBucket();
        try {
            String originalFilename = bucket + "/" + UUID.randomUUID() + multipartFile.getOriginalFilename();

            ObjectMetadata metadata = createObjectMetadata(multipartFile);

            PutObjectRequest request = new PutObjectRequest(bucket, originalFilename, multipartFile.getInputStream(), metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead);
            amazonS3Client.putObject(request);

            return amazonS3Client.getUrl(bucket, originalFilename).toString();
        } catch (Exception e) {
            throw new CloudException();
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
