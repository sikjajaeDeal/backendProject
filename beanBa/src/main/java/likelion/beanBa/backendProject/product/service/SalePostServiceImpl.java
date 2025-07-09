package likelion.beanBa.backendProject.product.service;

import jakarta.persistence.EntityNotFoundException;
import likelion.beanBa.backendProject.member.Entity.Member;
import likelion.beanBa.backendProject.member.repository.MemberRepository;
import likelion.beanBa.backendProject.product.dto.SalePostRequest;
import likelion.beanBa.backendProject.product.dto.SalePostResponse;
import likelion.beanBa.backendProject.product.entity.Category;
import likelion.beanBa.backendProject.product.entity.SalePost;
import likelion.beanBa.backendProject.product.entity.SalePostImage;
import likelion.beanBa.backendProject.product.product_enum.Yn;
import likelion.beanBa.backendProject.product.repository.CategoryRepository;
import likelion.beanBa.backendProject.product.repository.SalePostImageRepository;
import likelion.beanBa.backendProject.product.repository.SalePostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SalePostServiceImpl implements SalePostService {

    private final SalePostRepository salePostRepository;
    private final CategoryRepository categoryRepository;
    private final SalePostImageRepository salePostImageRepository;
    private final MemberRepository memberRepository;


    /**
     * 게시글 생성
     */
    @Override
    @Transactional
    public SalePost createPost(SalePostRequest salePostRequest, Member sellerPk) {
        Category categoryPk = categoryRepository.findById(salePostRequest.getCategoryPk())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다."));

        SalePost salePost = SalePost.create(
                sellerPk,
                categoryPk,
                salePostRequest.getTitle(),
                salePostRequest.getContent(),
                salePostRequest.getHopePrice(),
                salePostRequest.getLatitude(),
                salePostRequest.getLongitude()
        );

        salePostRepository.save(salePost);
        saveImages(salePost, salePostRequest.getImageUrls());

        return salePost;
    }

    /**
     * 게시글 전체 조회
     */
    @Override
    @Transactional(readOnly = true)
    public List<SalePostResponse> getAllPosts() {
        List<SalePost> salePosts = salePostRepository.findAllByDeleteYn(Yn.N);

        return salePosts.stream()
                .map(salePost -> {
                    List<String> imageUrls = salePostImageRepository.findAllByPostPkAndDeleteYn(salePost, Yn.N)
                            .stream()
                            .map(SalePostImage::getImageUrl)
                            .toList();
                    return SalePostResponse.from(salePost, imageUrls);
                })
                .toList();
    }

    /**
     * 게시글 단건 조회
     */
    @Override
    @Transactional(readOnly = true)
    public SalePostResponse getPost(Long postPk) {
        SalePost salePost = findPostById(postPk);

        List<String> imageUrls = salePostImageRepository.findAllByPostPkAndDeleteYn(salePost, Yn.N)
                .stream()
                .map(SalePostImage::getImageUrl)
                .toList();

        return SalePostResponse.from(salePost, imageUrls);
    }


    /**
     * 게시글 수정
     */
    @Override
    @Transactional
    public void updatePost(Long postPk, SalePostRequest salePostRequest, Member sellerPk) {
        SalePost salePost = findPostById(postPk);

        if (!salePost.getSellerPk().getMemberPk().equals(sellerPk.getMemberPk())) {
            throw new IllegalArgumentException("작성자만 수정할 수 있습니다.");
        }

        Category category = categoryRepository.findById(salePostRequest.getCategoryPk())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다."));

        salePost.update(
                salePostRequest.getTitle(),
                salePostRequest.getContent(),
                salePostRequest.getHopePrice(),
                salePostRequest.getLatitude(),
                salePostRequest.getLongitude(),
                category
        );


        // 🔁 이미지 무조건 삭제 후 재등록
        List<String> newUrls = salePostRequest.getImageUrls();
        if (newUrls != null && !newUrls.isEmpty()) {
            salePostImageRepository.findAllByPostPkAndDeleteYn(salePost, Yn.N)
                    .forEach(SalePostImage::markAsDeleted);

            saveImages(salePost, newUrls);
        }
    }

    /**
     * 게시글 삭제 (소프트 삭제)
     */
    @Override
    @Transactional
    public void deletePost(Long postPk, Member sellerPk) {
        SalePost salePost = findPostById(postPk);

        if (!salePost.getSellerPk().getMemberPk().equals(sellerPk.getMemberPk())) {
            throw new IllegalArgumentException("작성자만 삭제할 수 있습니다.");
        }

        salePost.markAsDeleted();

        salePostImageRepository.findAllByPostPkAndDeleteYn(salePost, Yn.N)
                .forEach(SalePostImage::markAsDeleted);
    }

    /**
     * 게시글 단건 조회 헬퍼
     */
    private SalePost findPostById(Long postPk) {
        return salePostRepository.findByPostPkAndDeleteYn(postPk, Yn.N)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않거나 삭제된 게시글입니다."));
    }

    /**
     * 이미지 저장 헬퍼
     */
    private void saveImages(SalePost salePost, List<String> imageUrls) {
        if (imageUrls == null || imageUrls.isEmpty()) return;

        List<SalePostImage> images = imageUrls.stream()
                .map(url -> SalePostImage.of(salePost, url))
                .toList();

        salePostImageRepository.saveAll(images);
    }


    /**
     * 이미지 변경 여부 감지
     */
    private boolean isImageUpdated(SalePost postPk, List<String> newUrls) {
        List<String> oldUrls = salePostImageRepository.findAllByPostPkAndDeleteYn(postPk, Yn.N)
                .stream()
                .map(SalePostImage::getImageUrl)
                .toList();

        return !new java.util.HashSet<>(oldUrls).equals(new java.util.HashSet<>(newUrls));
    }

    @Transactional
    public void completeSale(Long postPk, Long buyerPk, Member sellerPk) {
        SalePost salePost = salePostRepository.findById(postPk)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        if (!salePost.getSellerPk().getMemberPk().equals(sellerPk.getMemberPk())) {
            throw new AccessDeniedException("해당 게시글의 판매자가 아닙니다.");
        }

        Member buyer = memberRepository.findById(buyerPk)
                .orElseThrow(() -> new IllegalArgumentException("해당 구매자가 존재하지 않습니다."));

        salePost.markAsSold(buyer);
    }
}