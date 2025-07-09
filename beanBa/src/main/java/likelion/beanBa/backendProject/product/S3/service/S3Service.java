package likelion.beanBa.backendProject.product.S3.service;

//import com.amazonaws.services.s3.AmazonS3;
//import com.amazonaws.services.s3.model.PutObjectRequest;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class S3Service {
//
//    private final AmazonS3 amazonS3;
//
//    @Value("${cloud.aws.s3.bucket}")
//    private String bucketName;
//
//    //하나의 파일을 S3에 올리는 메소드
//    public String uploadFile(MultipartFile multipartFile) throws IOException {
//        //변환
//        File file = multiPartFileToFile(multipartFile);
//
//        //파일이름을 원본 이름과 현재시간으로 고유하게 만듬
//        String fileName = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();
//
//        //S3 전송
//        amazonS3.putObject(new PutObjectRequest(bucketName, fileName, file));
//
//        //임시 파일 삭제
//        file.delete();
//        return fileName;
//    }
//
//    private File multiPartFileToFile(MultipartFile file) throws IOException {
//        //변환 하려는 MultipartFile 객체의 이름으로 file 객체 생성
//        File convertedFile = new File(file.getOriginalFilename());
//        try (FileOutputStream fileOutputStream = new FileOutputStream(convertedFile)) {
//            fileOutputStream.write(file.getBytes());
//        }
//        return convertedFile;
//    }
//
//    //다중 파일을 S3에 올리는 메소드
//    public List<String> uploadFiles(MultipartFile[] files) throws IOException {
//        List<String> fileNames = new ArrayList<String>();
//
//        for (MultipartFile file : files) {
//            try {
//                File f = multiPartFileToFile(file);
//                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
//                fileNames.add(fileName);
//                amazonS3.putObject(new PutObjectRequest(bucketName, fileName, f));
//                f.delete();
//            } catch (IOException e) {
//                System.out.println("File upload failed: " + e.getMessage());
//            }
//        }
//        return fileNames;
//    }
//}
