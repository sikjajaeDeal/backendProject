package likelion.beanBa.backendProject.product.service;


import likelion.beanBa.backendProject.member.Entity.Member;
import likelion.beanBa.backendProject.product.dto.SalePostRequest;
import likelion.beanBa.backendProject.product.dto.SalePostDetailResponse;
import likelion.beanBa.backendProject.product.dto.SalePostSummaryResponse;
import likelion.beanBa.backendProject.product.entity.SalePost;

import java.util.List;

public interface SalePostService {

    SalePost createPost(SalePostRequest salePostRequest, Member sellerPk);

    List<SalePostSummaryResponse> getAllPosts(Member member);

    SalePostDetailResponse getPost(Long postPk, Member member);

    void updatePost(Long postPk, SalePostRequest salePostRequest, Member sellerPk);

    void deletePost(Long postPk, Member sellerPk);

    void completeSale(Long postPk, Long buyerPk, Member sellerPk);

}
