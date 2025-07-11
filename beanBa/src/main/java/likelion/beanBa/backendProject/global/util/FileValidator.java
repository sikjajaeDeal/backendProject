package likelion.beanBa.backendProject.global.util;

import org.springframework.web.multipart.MultipartFile;

public class FileValidator {

    /**
     * 파일이 null 이거나 비어있거나 너무 많으면 예외 발생
     */
    public static void validateImageFiles(MultipartFile[] files, int maxCount) {
        if (files == null || files.length == 0 || allFilesEmpty(files)) {
            throw new IllegalArgumentException("이미지를 1개 이상 등록해야 합니다.");
        }

        if (files.length > maxCount) {
            throw new IllegalArgumentException("이미지는 최대 " + maxCount + "개까지만 등록할 수 있습니다.");
        }
    }

    private static boolean allFilesEmpty(MultipartFile[] files) {
        for (MultipartFile file : files) {
            if (file != null && !file.isEmpty()) {
                return false;
            }
        }
        return true;
    }
}