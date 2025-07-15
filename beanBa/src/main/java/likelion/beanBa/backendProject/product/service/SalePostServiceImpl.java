package likelion.beanBa.backendProject.product.service;

import jakarta.persistence.EntityNotFoundException;
import likelion.beanBa.backendProject.like.repository.SalePostLikeRepository;
import likelion.beanBa.backendProject.member.Entity.Member;
import likelion.beanBa.backendProject.member.repository.MemberRepository;
import likelion.beanBa.backendProject.product.dto.SalePostRequest;
import likelion.beanBa.backendProject.product.dto.SalePostDetailResponse;
import likelion.beanBa.backendProject.product.dto.SalePostSummaryResponse;
import likelion.beanBa.backendProject.product.elasticsearch.dto.SalePostEsDocument;
import likelion.beanBa.backendProject.product.elasticsearch.service.SalePostEsService;
import likelion.beanBa.backendProject.product.entity.Category;
import likelion.beanBa.backendProject.product.entity.SalePost;
import likelion.beanBa.backendProject.product.entity.SalePostImage;
import likelion.beanBa.backendProject.product.product_enum.Yn;
import likelion.beanBa.backendProject.product.repository.CategoryRepository;
import likelion.beanBa.backendProject.product.repository.SalePostImageRepository;
import likelion.beanBa.backendProject.product.repository.SalePostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SalePostServiceImpl implements SalePostService {

    private final SalePostRepository salePostRepository;
    private final CategoryRepository categoryRepository;
    private final SalePostImageRepository salePostImageRepository;
    private final MemberRepository memberRepository;
    private final SalePostLikeRepository salePostLikeRepository;
    private final SalePostEsService salePostEsService;

    /** 게시글 생성 **/
    @Override
    @Transactional
    public SalePost createPost(SalePostRequest salePostRequest, Member sellerPk) {
        Category categoryPk = categoryRepository.findById(salePostRequest.getCategoryPk())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 카테고리입니다."));

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


        //테스트할 때 주석처리
        salePostEsService.save(salePost); // 게시글 생성 시 Elasticsearch에 저장


        return salePost;
    }


    /** 게시글 전체 조회 **/
    @Override
    @Transactional(readOnly = true)
    public List<SalePostSummaryResponse> getAllPosts(Member member) {
        List<SalePost> salePosts = salePostRepository.findAllByDeleteYn(Yn.N);

        // 찜한 게시글 postPk 만 먼저 가져오기
        Set<Long> likedPostPks = member != null
                ? salePostLikeRepository.findAllByMemberPk(member).stream()
                .map(like -> like.getPostPk().getPostPk())
                .collect(Collectors.toSet())
                : Set.of();

        return salePosts.stream()
                .map(salePost -> {
                    // 삭제되지 않은 이미지만 가져오기
                    List<SalePostImage> images = salePostImageRepository
                            .findAllByPostPkAndDeleteYn(salePost, Yn.N);

                    // 썸네일 추출
                    String thumbnailUrl = images.stream()
                            .findFirst()
                            .map(SalePostImage::getImageUrl)
                            .orElse(null);

                    boolean salePostLiked = likedPostPks.contains(salePost.getPostPk()); // 찜 여부 판단

                    int likeCount = salePostLikeRepository.countByPostPk(salePost); // 찜 수 조회

                    return SalePostSummaryResponse.from(salePost, thumbnailUrl, salePostLiked, likeCount);
                })
                .toList();
    }


    /** 게시글 단건 조회 **/
    @Override
    @Transactional //조회수 DB 반영 필요
    public SalePostDetailResponse getPost(Long postPk, Member member) {

        SalePost salePost = findPostById(postPk);

        // 조회수 증가
        salePost.increaseViewCount();

        List<String> imageUrls = salePostImageRepository.findAllByPostPkAndDeleteYn(salePost, Yn.N)
                .stream()
                .map(SalePostImage::getImageUrl)
                .toList();

        // 단건 조회 시에도 찜 여부 확인
        boolean salePostLiked = member != null && salePostLikeRepository.existsByMemberPkAndPostPk(member, salePost);

        int likeCount = salePostLikeRepository.countByPostPk(salePost); // 찜 수 조회

        return SalePostDetailResponse.from(salePost, imageUrls, salePostLiked, likeCount);
    }



    /** 게시글 수정 **/
    @Override
    @Transactional
    public void updatePost(Long postPk, SalePostRequest salePostRequest, Member sellerPk) {
        SalePost salePost = findPostById(postPk);

        if (!salePost.getSellerPk().getMemberPk().equals(sellerPk.getMemberPk())) {
            throw new IllegalArgumentException("작성자만 수정할 수 있습니다.");
        }

        Category category = categoryRepository.findById(salePostRequest.getCategoryPk())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 카테고리입니다."));

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

        // 테스트시 주석처리
        salePostEsService.update(salePost); // Elasticsearch에서 게시글 업데이트
    }


    /** 판매와료 처리 시 호출 **/
    @Transactional
    public void completeSale(Long postPk, Long buyerPk, Member sellerPk) { //sellerPk 는 로그인된 사용자 정보이므로 Member 객체로 받기
        SalePost salePost = salePostRepository.findById(postPk)
                .orElseThrow(() -> new EntityNotFoundException("해당 게시글이 존재하지 않습니다."));

        validateWriter(salePost, sellerPk);

        Member buyer = null; //분기에 따라 markAsSold 를 중복하지 않기 위해

        if (buyerPk != null) {
            buyer = memberRepository.findById(buyerPk)
                    .orElseThrow(() -> new EntityNotFoundException("해당 구매자가 존재하지 않습니다."));
        } else {
            log.info("구매자 없이 판매자가 거래완료 처리했습니다.");
        }

        salePost.markAsSold(buyer);
    }


    /** #### 헬퍼 메소드 #### **/


    /** 게시글 삭제 (소프트 삭제) **/
    @Override
    @Transactional
    public void deletePost(Long postPk, Member sellerPk) {
        SalePost salePost = findPostById(postPk);

        validateWriter(salePost, sellerPk);

        salePost.markAsDeleted();

        // 테스트시 주석처리
        salePostEsService.delete(salePost); // Elasticsearch에서 게시글 삭제

        salePostImageRepository.findAllByPostPkAndDeleteYn(salePost, Yn.N)
                .forEach(SalePostImage::markAsDeleted);
    }


    /** 게시글 단건 조회 헬퍼 **/
    private SalePost findPostById(Long postPk) {
        return salePostRepository.findByPostPkAndDeleteYn(postPk, Yn.N)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않거나 삭제된 게시글입니다."));
    }


    /** 이미지 저장 헬퍼 **/
    private void saveImages(SalePost salePost, List<String> imageUrls) {
        if (imageUrls == null || imageUrls.isEmpty()) return;

        List<SalePostImage> images = imageUrls.stream()
                .map(url -> SalePostImage.of(salePost, url))
                .toList();

        salePostImageRepository.saveAll(images);
    }


    /** 작성자 권한 확인 헬퍼 **/
    private void validateWriter(SalePost salePost, Member writer) {
        if (!salePost.getSellerPk().getMemberPk().equals(writer.getMemberPk())) {
            throw new AccessDeniedException("작성자만 수행할 수 있습니다.");
        }
    }


}