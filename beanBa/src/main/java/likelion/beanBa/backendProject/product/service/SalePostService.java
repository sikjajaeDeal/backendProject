package likelion.beanBa.backendProject.product.service;


import likelion.beanBa.backendProject.member.Entity.Member;
import likelion.beanBa.backendProject.product.dto.*;
import likelion.beanBa.backendProject.product.entity.SalePost;
import likelion.beanBa.backendProject.product.product_enum.SaleStatement;

import java.util.List;

public interface SalePostService {

    SalePost createPost(SalePostCreateRequest salePostCreateRequest, Member sellerPk);

    PageResponse<SalePostSummaryResponse> getAllPosts(Member member, int page, int size);

    SalePostDetailResponse getPost(Long postPk, Member member);

    void updatePost(Long postPk, SalePostRequest salePostRequest, Member sellerPk);

    void deletePost(Long postPk, Member sellerPk);

    String changeSaleStatus(Long postPk, SaleStatement newStatus, Long buyerPk, Member sellerPk);

}
