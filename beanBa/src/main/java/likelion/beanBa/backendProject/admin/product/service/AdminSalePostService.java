package likelion.beanBa.backendProject.admin.product.service;

import likelion.beanBa.backendProject.member.Entity.Member;
import likelion.beanBa.backendProject.product.dto.*;
import likelion.beanBa.backendProject.product.entity.Category;

import java.util.List;

public interface AdminSalePostService {


    PageResponse<SalePostSummaryResponse> getAllPostsAdmin(int page, int size, boolean includeDeleted);

    SalePostDetailResponse getPostDetailAdmin(Long postPk);

    void updateSalePostAdmin(Long postPk, SalePostRequest request);

    void deleteSalePostAdmin(Long postPk);

    List<Category> getAllCategory();
}
