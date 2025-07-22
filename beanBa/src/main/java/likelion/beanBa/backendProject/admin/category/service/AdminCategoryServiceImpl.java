package likelion.beanBa.backendProject.admin.category.service;


import likelion.beanBa.backendProject.product.dto.CategoryRequest;
import likelion.beanBa.backendProject.product.dto.CategoryResponse;
import likelion.beanBa.backendProject.product.dto.PageResponse;
import likelion.beanBa.backendProject.product.entity.Category;
import likelion.beanBa.backendProject.product.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AdminCategoryServiceImpl implements AdminCategoryService {

    private final CategoryRepository categoryRepository;

    public PageResponse<CategoryResponse> getAllCategory(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Category> categoryPage = categoryRepository.findAll(pageable);

        // 기존의 MemberResponse를 사용하여 반환
        Page<CategoryResponse> responsePage = categoryPage.map(CategoryResponse::from);

        System.out.println("토탈 페이지 : " + categoryPage.getTotalPages());
        return PageResponse.from(responsePage);

    }


    /**
     * 카테고리 생성
     **/
    @Transactional
    public CategoryResponse createCategory(CategoryRequest categoryRequest) {
        Category parent = null;
        System.out.println("카테고리생성 도착");
        if (categoryRequest.getParentPk() != null) {
            parent = categoryRepository.findById(categoryRequest.getParentPk())
                    .orElseThrow(() -> new IllegalArgumentException("상위 카테고리를 찾을 수 없습니다."));
        }
        System.out.println("카테고리 서비스 시작");
        Category category = Category.create(categoryRequest,parent);

        return CategoryResponse.from(categoryRepository.save(category));
    }


    /**카테고리 수정**/
    @Transactional
    public void updateCategoryAdmin(List<CategoryRequest> categoryRequsetList) {
        System.out.println("🛠️ 카테고리 수정 시작");
        System.out.println("🛠️ 카테고리 수정 시작");

        for (CategoryRequest req : categoryRequsetList) {
            if (req.getCategoryName() == null || req.getCategoryPk() == null) {
                throw new IllegalArgumentException("카테고리 이름과 PK는 필수입니다.");
            }

            Category category = categoryRepository.findById(req.getCategoryPk())
                    .orElseThrow(() -> new IllegalArgumentException("ID: " + req.getCategoryPk() + "에 해당하는 카테고리가 없습니다."));

            if ("Y".equals(category.getDeleteYn())) {
                if (req.getDeleteYn() != null) {
                    category.updateDeleteYn(req.getDeleteYn());
                    categoryRepository.save(category);
                    System.out.println("♻️ 삭제된 카테고리 deleteYn만 수정 완료 (ID: " + category.getCategoryPk() + ")");
                } else {
                    System.out.println("⛔ 삭제된 카테고리는 deleteYn만 수정할 수 있습니다. (ID: " + category.getCategoryPk() + ")");
                }
                continue;
            }
            Category parent =null;

            // 상위 카테고리 변경
            if (req.getParentPk() != null) {
                if (req.getParentPk().equals(category.getCategoryPk())) {
                    throw new IllegalArgumentException("카테고리 ID " + req.getCategoryPk() + "는 자기 자신을 상위로 지정할 수 없습니다.");
                }
                parent = categoryRepository.findById(req.getParentPk())
                        .orElseThrow(() -> new IllegalArgumentException("상위 카테고리 ID: " + req.getParentPk() + "를 찾을 수 없습니다."));

            }

            category.update(req, parent);
            categoryRepository.save(category);
        }

    }


    /**카테고리 삭제 함수(여러개 선택하여 삭제)**/

    @Transactional
    public void deleteCategoryAdmin(List<Long> categoryPkList) {
        for (Long id : categoryPkList) {
            Category category = categoryRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("카테고리 ID 없음 : " + id));

            category.delete();
            categoryRepository.save(category);
        }
    }

}
