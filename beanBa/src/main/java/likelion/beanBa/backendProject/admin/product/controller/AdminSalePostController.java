package likelion.beanBa.backendProject.admin.product.controller;


import likelion.beanBa.backendProject.admin.product.service.AdminSalePostService;
import likelion.beanBa.backendProject.global.util.InputValidator;
import likelion.beanBa.backendProject.product.S3.service.S3Service;
import likelion.beanBa.backendProject.product.dto.*;
import likelion.beanBa.backendProject.product.entity.Category;
import likelion.beanBa.backendProject.product.product_enum.Yn;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/sale-post")
public class AdminSalePostController {

    private final AdminSalePostService adminSalePostService;
    private final S3Service s3Service;



    @GetMapping
    public ResponseEntity<PageResponse<AdminSalePostSummaryResponse>> getAllPostsAdmin(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "false") boolean includeDeleted
    ) {
        System.out.println("product controller start");
        PageResponse<AdminSalePostSummaryResponse> posts = adminSalePostService.getAllPostsAdmin(page, size, includeDeleted);
        return ResponseEntity.ok(posts);
    }

    /** 조회 **/
    @GetMapping("/{postPk}")
    public ResponseEntity<AdminSalePostDetailResponse> getPostDetailAdmin(@PathVariable Long postPk) {
        AdminSalePostDetailResponse postDetail = adminSalePostService.getPostDetailAdmin(postPk);
        return ResponseEntity.ok(postDetail);
    }

    /**상품 수정하기**/

    @PutMapping("/{postPk}")
    public ResponseEntity<?> updateSalePostAdmin(
            @PathVariable Long postPk,
            @RequestPart("salePostRequest") SalePostRequest request,
            @RequestPart(value = "salePostImages", required = false) MultipartFile[] salePostImages,
            @RequestParam("deleteYn") Yn deleteYn
            ) {
        try {
            System.out.println("update salePostAdmin controller  start");
            InputValidator.validateHopePrice(request.getHopePrice()); // ✅ 희망 가격 검증 추가


            List<String> fullImageUrls = request.getImageUrls(); // 슬롯 순서 유지
            List<String> newImageUrls = new ArrayList<>();

            // 이미지 존재 여부 검증 (기존 + 새 이미지 모두 없음 → 에러)
            boolean noExistingImages = fullImageUrls == null || fullImageUrls.stream().allMatch(url -> url == null || url.isBlank());
            boolean noNewImages = salePostImages == null || Arrays.stream(salePostImages).allMatch(f -> f == null || f.isEmpty());

            if (noExistingImages && noNewImages) {
                throw new IllegalArgumentException("최소 1장의 이미지를 등록해야 합니다.");
            }

            //에러 검증 이후 기본 로직 진행
            if (salePostImages != null) {
                List<MultipartFile> validFiles = Arrays.stream(salePostImages)
                        .filter(f -> f != null && !f.isEmpty())
                        .collect(Collectors.toList());

                if (!validFiles.isEmpty()) {
                    newImageUrls = s3Service.uploadFiles(validFiles.toArray(new MultipartFile[0]));

                    // 새 이미지 URL 을 빈 슬롯(null or "")에 순서대로 채워넣기
                    int newImageIndex = 0;
                    for (int i = 0; i < fullImageUrls.size(); i++) {
                        String url = fullImageUrls.get(i);
                        if ((url == null || url.isBlank()) && newImageIndex < newImageUrls.size()) {
                            fullImageUrls.set(i, newImageUrls.get(newImageIndex++));
                        }
                    }

                    if (newImageIndex < newImageUrls.size()) {
                        throw new IllegalArgumentException("빈 이미지 슬롯보다 업로드한 이미지 수가 더 많습니다.");
                    }
                }
            }


            request.setImageUrls(fullImageUrls); //최종 슬롯 순서 반영

            System.out.println("update salePostAdmin controller end");
            adminSalePostService.updateSalePostAdmin(postPk, request, deleteYn);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.out.println("수정 실패 : "+e.getMessage());
            return ResponseEntity.badRequest().body("수정 실패: " + e.getMessage());
        }
    }

    // 삭제 (soft delete, DELETE)
    @DeleteMapping("/{postPk}")
    public ResponseEntity<?> deleteSalePostAdmin(@PathVariable Long postPk) {
        try {
            adminSalePostService.deleteSalePostAdmin(postPk);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("삭제 실패: " + e.getMessage());
        }
    }


    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getCategories() {
        List<Category> categories = adminSalePostService.getAllCategory();
        return ResponseEntity.ok(categories);
    }

}
