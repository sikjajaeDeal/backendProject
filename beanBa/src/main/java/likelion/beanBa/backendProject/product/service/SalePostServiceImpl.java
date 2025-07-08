package likelion.beanBa.backendProject.product.service;

import likelion.beanBa.backendProject.member.Entity.Member;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SalePostServiceImpl implements SalePostService {

    private final SalePostRepository salePostRepository;
    private final CategoryRepository categoryRepository;
    private final SalePostImageRepository salePostImageRepository;

    /**
     * 게시글 생성
     */
    @Override
    @Transactional
    public SalePost createPost(SalePostRequest request, Member seller) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다."));

        SalePost salePost = SalePost.create(
                seller,
                category,
                request.getTitle(),
                request.getContent(),
                request.getHopePrice(),
                request.getLatitude(),
                request.getLongitude()
        );

        salePostRepository.save(salePost);
        saveImages(request.getImageUrls(), salePost);

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
                    List<String> imageUrls = salePostImageRepository.findAllByPostAndDeleteYn(salePost, Yn.N)
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
    public SalePostResponse getPost(Long postId) {
        SalePost salePost = findPostById(postId);

        List<String> imageUrls = salePostImageRepository.findAllByPostAndDeleteYn(salePost, Yn.N)
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
    public void updatePost(Long postId, SalePostRequest request, Member seller) {
        SalePost salePost = findPostById(postId);

        if (!salePost.getSeller().getMemberPk().equals(seller.getMemberPk())) {
            throw new IllegalArgumentException("작성자만 수정할 수 있습니다.");
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다."));

        salePost.update(
                request.getTitle(),
                request.getContent(),
                request.getHopePrice(),
                request.getLatitude(),
                request.getLongitude(),
                category
        );


        // 🔁 이미지 변경 감지 추가됨
        List<String> newUrls = request.getImageUrls();
        if (newUrls != null && isImageUpdated(salePost, newUrls)) {
            // 기존 이미지 soft delete
            salePostImageRepository.findAllByPostAndDeleteYn(salePost, Yn.N)
                    .forEach(SalePostImage::markAsDeleted);

            // 새 이미지 등록
            saveImages(newUrls, salePost);
        }
    }

    /**
     * 게시글 삭제 (소프트 삭제)
     */
    @Override
    @Transactional
    public void deletePost(Long postId, Member seller) {
        SalePost salePost = findPostById(postId);

        if (!salePost.getSeller().getMemberPk().equals(seller.getMemberPk())) {
            throw new IllegalArgumentException("작성자만 삭제할 수 있습니다.");
        }

        salePost.markAsDeleted();

        salePostImageRepository.findAllByPostAndDeleteYn(salePost, Yn.N)
                .forEach(SalePostImage::markAsDeleted);
    }

    /**
     * 게시글 단건 조회 헬퍼
     */
    private SalePost findPostById(Long id) {
        return salePostRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
    }

    /**
     * 이미지 저장 헬퍼
     */
    private void saveImages(List<String> imageUrls, SalePost post) {
        if (imageUrls == null || imageUrls.isEmpty()) return;

        List<SalePostImage> images = imageUrls.stream()
                .map(url -> SalePostImage.of(post, url))
                .toList();

        salePostImageRepository.saveAll(images);
    }


    /**
     * 이미지 변경 여부 감지
     */
    private boolean isImageUpdated(SalePost post, List<String> newUrls) {
        List<String> oldUrls = salePostImageRepository.findAllByPostAndDeleteYn(post, Yn.N)
                .stream()
                .map(SalePostImage::getImageUrl)
                .toList();

        return !new java.util.HashSet<>(oldUrls).equals(new java.util.HashSet<>(newUrls));
    }
}