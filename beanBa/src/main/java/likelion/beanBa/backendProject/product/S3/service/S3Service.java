package likelion.beanBa.backendProject.product.S3.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    // 파일 1개 업로드
    public String uploadFile(MultipartFile multipartFile) throws IOException {
        File file = multiPartFileToFile(multipartFile);

        String fileName = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();

        amazonS3.putObject(new PutObjectRequest(bucketName, fileName, file));

        //임시 파일 삭제
        file.delete();

        // ✅ 변경됨: 파일 이름 대신 전체 URL 반환
        return amazonS3.getUrl(bucketName, fileName).toString();
    }

    // 파일 여러 개 업로드
    public List<String> uploadFiles(MultipartFile[] files) throws IOException {
        List<String> fileUrls = new ArrayList<>(); // ✅ 변경됨: fileNames → fileUrls

        for (MultipartFile file : files) {
            try {
                File f = multiPartFileToFile(file);
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

                amazonS3.putObject(new PutObjectRequest(bucketName, fileName, f));
                f.delete();

                // ✅ 변경됨: 전체 URL 반환
                String fileUrl = amazonS3.getUrl(bucketName, fileName).toString();
                fileUrls.add(fileUrl);
            } catch (IOException e) {
                log.error("File upload failed for {}: {}", file.getOriginalFilename(), e.getMessage());
            }
        }
        return fileUrls;
    }

    // ✅ 파일 변환 경로 변경
    private File multiPartFileToFile(MultipartFile file) throws IOException {
        //변환 하려는 MultipartFile 객체의 이름으로 file 객체 생성
        // ✅ 변경됨: OS 안전 경로 사용
        File convertedFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
        try (FileOutputStream fileOutputStream = new FileOutputStream(convertedFile)) {
            fileOutputStream.write(file.getBytes());
        }
        return convertedFile;
    }
}