package likelion.beanBa.backendProject.admin.category.service;


import likelion.beanBa.backendProject.member.dto.AdminMemberDTO;
import likelion.beanBa.backendProject.product.dto.CategoryRequest;
import likelion.beanBa.backendProject.product.dto.CategoryResponse;
import likelion.beanBa.backendProject.product.dto.PageResponse;

import java.util.List;

public interface AdminCategoryService {

    PageResponse<CategoryResponse> getAllCategory(int page, int size);

    CategoryResponse createCategory(CategoryRequest categoryRequest);

    void updateCategoryAdmin(List<CategoryRequest> categoryRequsetList);

    void deleteCategoryAdmin(List<Long> categoryPkList);
}
