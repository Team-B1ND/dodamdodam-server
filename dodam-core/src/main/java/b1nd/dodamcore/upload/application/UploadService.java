package b1nd.dodamcore.upload.application;

import org.springframework.web.multipart.MultipartFile;

public interface UploadService {

    String uploadFile(MultipartFile multipartFile);
}
