package b1nd.dodamapi.upload;

import b1nd.dodamapi.common.response.ResponseData;
import b1nd.dodamcore.upload.application.UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/upload")
@RequiredArgsConstructor
public class    UploadController {

    private final UploadService uploadService;

    @PostMapping
    public ResponseData<String> uploadFile(@RequestParam("file") MultipartFile multipartFile) {
        String fileUrl = uploadService.uploadFile(multipartFile);
        return ResponseData.created("파일 업로드 성공", fileUrl);
    }
}
