package likelion.beanBa.backendProject.admin.product.service;

import likelion.beanBa.backendProject.member.Entity.Member;
import likelion.beanBa.backendProject.product.dto.*;
import likelion.beanBa.backendProject.product.entity.Category;
import likelion.beanBa.backendProject.product.product_enum.Yn;

import java.util.List;

public interface AdminSalePostService {


    PageResponse<AdminSalePostSummaryResponse> getAllPostsAdmin(int page, int size, boolean includeDeleted);

    AdminSalePostDetailResponse getPostDetailAdmin(Long postPk);

    void updateSalePostAdmin(Long postPk, SalePostRequest request, Yn deleteYn);

    void deleteSalePostAdmin(Long postPk);

    List<Category> getAllCategory();
}
