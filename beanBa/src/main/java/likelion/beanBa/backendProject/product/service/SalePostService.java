package likelion.beanBa.backendProject.product.service;


import likelion.beanBa.backendProject.member.Entity.Member;
import likelion.beanBa.backendProject.product.dto.SalePostRequest;
import likelion.beanBa.backendProject.product.dto.SalePostResponse;
import likelion.beanBa.backendProject.product.entity.SalePost;

import java.util.List;

public interface SalePostService {

    SalePost createPost(SalePostRequest request, Member seller);

    List<SalePostResponse> getAllPosts();

    SalePostResponse getPost(Long postId);

    void updatePost(Long postId, SalePostRequest request, Member seller);

    void deletePost(Long postId, Member seller);
}
