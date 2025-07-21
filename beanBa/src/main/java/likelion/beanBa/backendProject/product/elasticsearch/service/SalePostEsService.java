package likelion.beanBa.backendProject.product.elasticsearch.service;

import likelion.beanBa.backendProject.like.repository.SalePostLikeRepository;
import likelion.beanBa.backendProject.member.Entity.Member;
import likelion.beanBa.backendProject.product.dto.SalePostSummaryResponse;
import likelion.beanBa.backendProject.product.elasticsearch.dto.SalePostEsDocument;
import likelion.beanBa.backendProject.product.elasticsearch.dto.SalePostEsTestDocument;
import likelion.beanBa.backendProject.product.elasticsearch.dto.SearchRequestDTO;
import likelion.beanBa.backendProject.product.entity.SalePost;
import likelion.beanBa.backendProject.product.repository.SalePostImageRepository;
import likelion.beanBa.backendProject.product.repository.SalePostRepository;
import org.springframework.data.domain.Page;

public interface SalePostEsService {

  Page<SalePostSummaryResponse> search(SearchRequestDTO searchRequestDTO, Member member);
  Page<SalePostSummaryResponse> search_test(SearchRequestDTO searchRequestDTO, Member member);

  void save(SalePost salePost);

  void save_test(Member member, SalePost salePost, SalePostRepository salePostRepository,
      SalePostLikeRepository salePostLikeRepository, SalePostImageRepository salePostImageRepository);

  void delete(SalePost salePost);

  void update(SalePost salePost);

}
