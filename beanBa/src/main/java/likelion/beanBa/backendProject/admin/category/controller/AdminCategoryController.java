package likelion.beanBa.backendProject.admin.category.controller;


import likelion.beanBa.backendProject.admin.category.service.AdminCategoryService;
import likelion.beanBa.backendProject.member.dto.AdminMemberDTO;
import likelion.beanBa.backendProject.product.dto.CategoryRequest;
import likelion.beanBa.backendProject.product.dto.CategoryResponse;
import likelion.beanBa.backendProject.product.dto.PageResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/category")
public class AdminCategoryController {


    private final AdminCategoryService adminCategoryService;


    /**카테고리 조회**/
    @GetMapping
    public ResponseEntity<PageResponse<CategoryResponse>> getAllCategory(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10")int size
    ){
        System.out.println("🔥 Controller 도착함");
        PageResponse<CategoryResponse> response = adminCategoryService.getAllCategory(page, size);
        System.out.println("🔥 전체 카테고리 수: " + response.getTotalElements());
        System.out.println("📄 현재 페이지 카테고리 수: " + response.getContent().size());

        return ResponseEntity.ok(response);
    }


    /**카테고리 생성**/
    @PostMapping("/create")
    public ResponseEntity<?> createCategory(@RequestBody CategoryRequest categoryRequest){
        System.out.println("🔥 생성 Controller 도착함");
        try {
            CategoryResponse response = adminCategoryService.createCategory(categoryRequest);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage()); // 400 Bad Request + 에러 메시지
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("서버 오류가 발생했습니다."); // 500 에러
        }
    }

    /**카테고리 수정**/
    @PutMapping("/update")
    public ResponseEntity<?> updateCategories(@RequestBody List<CategoryRequest> categoryRequests) {
        try {
            adminCategoryService.updateCategoryAdmin(categoryRequests);
            return ResponseEntity.ok("✅ 카테고리 수정 완료");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("서버 오류 발생");
        }
    }

    /**카테고리 삭제**/
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteCategories(@RequestBody List<Long> categoryPkList) {
        adminCategoryService.deleteCategoryAdmin(categoryPkList);
        return ResponseEntity.ok().build();

    }


}
