package likelion.beanBa.backendProject.admin.product.service;


import jakarta.persistence.EntityNotFoundException;
import likelion.beanBa.backendProject.like.repository.SalePostLikeRepository;
import likelion.beanBa.backendProject.product.dto.*;
import likelion.beanBa.backendProject.product.entity.Category;
import likelion.beanBa.backendProject.product.entity.SalePost;
import likelion.beanBa.backendProject.product.entity.SalePostImage;
import likelion.beanBa.backendProject.product.product_enum.Yn;
import likelion.beanBa.backendProject.product.repository.CategoryRepository;
import likelion.beanBa.backendProject.product.repository.SalePostImageRepository;
import likelion.beanBa.backendProject.product.repository.SalePostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AdminSalePostServiceImpl implements AdminSalePostService {
    private final SalePostRepository salePostRepository;
    private final SalePostImageRepository salePostImageRepository;
    private final SalePostLikeRepository salePostLikeRepository;
    private final CategoryRepository categoryRepository;



    @Transactional(readOnly = true)
    public PageResponse<AdminSalePostSummaryResponse> getAllPostsAdmin(int page, int size, boolean includeDeleted) {
        System.out.println("product getAllPostsAdmin start");
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "postAt"));


        Page<SalePost> salePostsPage = includeDeleted
                ? salePostRepository.findAll(pageable)
                : salePostRepository.findAllByDeleteYn(Yn.N, pageable);

        List<AdminSalePostSummaryResponse> content = salePostsPage.getContent().stream()
                .map(post -> {

                    List<SalePostImage> images = salePostImageRepository.findAllByPostPkAndDeleteYn(post, Yn.N);

                    String thumbnail = images.stream()
                            .sorted(Comparator.comparing(i -> i.getImageOrder() != null ? i.getImageOrder() : Integer.MAX_VALUE))
                            .map(SalePostImage::getImageUrl)
                            .findFirst()
                            .orElse(null);

                    return AdminSalePostSummaryResponse.from(post, thumbnail, false, 0); // 찜 기능 제외
                })
                .toList();

        return new PageResponse<>(
                content,
                salePostsPage.getNumber(),
                salePostsPage.getSize(),
                salePostsPage.getTotalElements(),
                salePostsPage.getTotalPages(),
                salePostsPage.isLast()
        );
    }


    /**제품 상세보기**/
    @Transactional(readOnly = true)
    public AdminSalePostDetailResponse getPostDetailAdmin(Long postPk) {
        SalePost post = salePostRepository.findById(postPk)
                .orElseThrow(() -> new RuntimeException("해당 게시글이 없습니다. postPk=" + postPk));

        List<SalePostImage> images = salePostImageRepository.findAllByPostPkAndDeleteYn(post, Yn.N);

        List<String> imageUrls = images.stream()
                .sorted(Comparator.comparing(i -> i.getImageOrder() != null ? i.getImageOrder() : Integer.MAX_VALUE))
                .map(SalePostImage::getImageUrl)
                .toList();

        // 찜 여부, 좋아요 개수 등도 추가 가능
        int likeCount = salePostLikeRepository.countByPostPk(post);
        boolean salePostLiked = false; // 관리자는 보통 상관없으니 false 고정 가능

        return AdminSalePostDetailResponse.from(post, imageUrls, salePostLiked, likeCount);
    }


    @Transactional
    public void updateSalePostAdmin(Long postPk, SalePostRequest request, Yn deleteYn) {
        SalePost post = salePostRepository.findById(postPk)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        Category category = categoryRepository.findById(request.getCategoryPk())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 카테고리입니다."));

        System.out.println("update salePostAdmin service start");

        post.update(
                request.getTitle(),
                request.getContent(),
                request.getHopePrice(),
                request.getLatitude(),
                request.getLongitude(),
                category
        );
        //post.setDeleteYn(request.getDeleteYn());
       post.markAsNotDeleted();

        // 기존 이미지 전체 삭제 처리 (소프트 삭제)
        List<SalePostImage> existingImages = salePostImageRepository.findAllByPostPkAndDeleteYn(post, Yn.N);
        existingImages.forEach(SalePostImage::markAsDeleted);

        // 현재 순서 그대로 새 이미지 등록
        List<String> requestUrls = request.getImageUrls(); // 순서 유지

        if (requestUrls != null) {
            for (int i = 0; i < requestUrls.size(); i++) {
                String url = requestUrls.get(i);
                if (url != null && !url.isBlank()) {
                    salePostImageRepository.save(SalePostImage.ofWithOrder(post, url, i));
                }
            }
        }
        System.out.println("update salePostAdmin service end");

        salePostRepository.save(post);
    }

    @Transactional
    public void deleteSalePostAdmin(Long postPk) {
        SalePost salePost = salePostRepository.findById(postPk)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        salePost.markAsDeleted();
        salePostImageRepository.findAllByPostPkAndDeleteYn(salePost, Yn.N)
                .forEach(SalePostImage::markAsDeleted);
        salePostRepository.save(salePost);
    }

    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }




}
