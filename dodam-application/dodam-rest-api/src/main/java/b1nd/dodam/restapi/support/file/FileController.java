package b1nd.dodam.restapi.support.file;

import b1nd.dodam.restapi.support.data.ResponseData;
import b1nd.dodam.simple.storage.service.client.SimpleStorageServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/upload")
@RequiredArgsConstructor
public class FileController {

    private final SimpleStorageServiceClient simpleStorageServiceClient;

    @PostMapping
    public ResponseData<String> uploadFile(@RequestParam("file") MultipartFile multipartFile) {
        return ResponseData.created("파일 업로드 성공", simpleStorageServiceClient.uploadFile(multipartFile));
    }

}
