package likelion.beanBa.backendProject.product.service;

import jakarta.persistence.EntityNotFoundException;
import likelion.beanBa.backendProject.like.repository.SalePostLikeRepository;
import likelion.beanBa.backendProject.member.Entity.Member;
import likelion.beanBa.backendProject.member.repository.MemberRepository;
import likelion.beanBa.backendProject.product.dto.*;
import likelion.beanBa.backendProject.product.elasticsearch.dto.SalePostEsDocument;
import likelion.beanBa.backendProject.product.elasticsearch.service.SalePostEsService;
import likelion.beanBa.backendProject.product.entity.Category;
import likelion.beanBa.backendProject.product.entity.SalePost;
import likelion.beanBa.backendProject.product.entity.SalePostImage;
import likelion.beanBa.backendProject.product.product_enum.SaleStatement;
import likelion.beanBa.backendProject.product.product_enum.Yn;
import likelion.beanBa.backendProject.product.repository.CategoryRepository;
import likelion.beanBa.backendProject.product.repository.SalePostImageRepository;
import likelion.beanBa.backendProject.product.repository.SalePostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
    public SalePost createPost(SalePostCreateRequest salePostCreateRequest, Member sellerPk) {
        Category categoryPk = categoryRepository.findById(salePostCreateRequest.getCategoryPk())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 카테고리입니다."));

        // Member 엔티티에서 위도, 경도 가져오기
        Double latitude = sellerPk.getLatitude();
        Double longitude = sellerPk.getLongitude();

        SalePost salePost = SalePost.create(
                sellerPk,
                categoryPk,
                salePostCreateRequest.getTitle(),
                salePostCreateRequest.getContent(),
                salePostCreateRequest.getHopePrice(),
                latitude,
                longitude
        );

        salePostRepository.save(salePost);
        saveImages(salePost, salePostCreateRequest.getImageUrls());


        //테스트할 때 주석처리
        salePostEsService.save(salePost); // 게시글 생성 시 Elasticsearch에 저장


        return salePost;
    }


    /** 게시글 전체 조회 **/
    @Override
    @Transactional(readOnly = true)
    public PageResponse<SalePostSummaryResponse> getAllPosts(Member member, int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "postAt"));
        Page<SalePost> salePostsPage = salePostRepository.findAllByDeleteYn(Yn.N, pageable);
        List<SalePost> salePosts = salePostsPage.getContent();

        // 찜한 게시글 postPk 만 먼저 가져오기
        Set<Long> likedPostPks = member != null
                ? salePostLikeRepository.findAllByMemberPk(member).stream()
                .map(like -> like.getPostPk().getPostPk())
                .collect(Collectors.toSet())
                : Set.of();

        List<SalePostSummaryResponse> SalePostContent = salePosts.stream()
                .map(salePost -> {
                    // 삭제되지 않은 이미지만 가져오기
                    List<SalePostImage> images = salePostImageRepository
                            .findAllByPostPkAndDeleteYn(salePost, Yn.N);

                    // 썸네일 추출
                    String thumbnailUrl = images.stream()
                            .sorted(Comparator.comparing(i -> i.getImageOrder() != null ? i.getImageOrder() : Integer.MAX_VALUE))
                            .map(SalePostImage::getImageUrl)
                            .findFirst()
                            .orElse(null);

                    boolean salePostLiked = likedPostPks.contains(salePost.getPostPk()); // 찜 여부 판단

                    int likeCount = salePostLikeRepository.countByPostPk(salePost); // 찜 수 조회

                    return SalePostSummaryResponse.from(salePost, thumbnailUrl, salePostLiked, likeCount);
                })
                .toList();

        return new PageResponse<>(
                SalePostContent,
                salePostsPage.getNumber(),
                salePostsPage.getSize(),
                salePostsPage.getTotalElements(),
                salePostsPage.getTotalPages(),
                salePostsPage.isLast()
        );

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
                .sorted(Comparator.comparing(i -> i.getImageOrder() != null ? i.getImageOrder() : Integer.MAX_VALUE))
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
        // 삭제되지 않은 게시글만 조회 (Yn.N 필터 포함)
        SalePost salePost = salePostRepository.findByPostPkAndDeleteYn(postPk, Yn.N)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않거나 삭제된 게시글입니다."));

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


        // 기존 이미지 전체 삭제 처리 (소프트 삭제)
        List<SalePostImage> existingImages = salePostImageRepository.findAllByPostPkAndDeleteYn(salePost, Yn.N);
        existingImages.forEach(SalePostImage::markAsDeleted);

        // 현재 순서 그대로 새 이미지 등록
        List<String> requestUrls = salePostRequest.getImageUrls(); // 순서 유지

        if (requestUrls != null) {
            for (int i = 0; i < requestUrls.size(); i++) {
                String url = requestUrls.get(i);
                if (url != null && !url.isBlank()) {
                    salePostImageRepository.save(SalePostImage.ofWithOrder(salePost, url, i));
                }
            }
        }

        // 테스트시 주석처리
        salePostEsService.update(salePost); // Elasticsearch에서 게시글 업데이트
    }


    /** 판매와료 처리 시 호출 **/
    @Transactional
    public String changeSaleStatus(Long postPk, SaleStatement newStatus, Long buyerPk, Member sellerPk) { //sellerPk 는 로그인된 사용자 정보이므로 Member 객체로 받기
        SalePost salePost = salePostRepository.findById(postPk)
                .orElseThrow(() -> new EntityNotFoundException("해당 게시글이 존재하지 않습니다."));

        validateWriter(salePost, sellerPk);

        if (newStatus == null) {
            throw new IllegalArgumentException("상태 값이 지정되지 않았습니다.");
        }

        switch (newStatus) {
            case S: // 판매중
            case H: // 판매보류
                if (salePost.getState() == newStatus) {
                    log.info("이미 '{}' 상태인 게시글입니다. 상태 변경 생략.", newStatus);
                    return "이미 " + newStatus.name() + " 상태입니다.";
                }

                salePost.changeState(newStatus);
                salePost.removeBuyer(); // 구매자 정보 제거
                log.info("게시글 [{}] 상태가 {}로 변경되었습니다. (구매자 정보 제거)", postPk, newStatus);
                return "판매 상태가 '" + newStatus.name() + "'로 변경되었습니다. (구매자 없음/제거)";

            case C: // 판매완료
                if (salePost.getState() == SaleStatement.C) {
                    Member currentBuyer = salePost.getBuyerPk();
                    if (buyerPk == null && currentBuyer == null) {
                        throw new IllegalStateException("이미 구매자 없이 판매 완료된 게시글입니다.");
                    }
                    if (buyerPk != null && currentBuyer != null &&
                            currentBuyer.getMemberPk().equals(buyerPk)) {
                        throw new IllegalStateException("이미 동일한 구매자로 판매 완료된 게시글입니다.");
                    }
                }

                if (buyerPk == null) {
                    log.info("판매자가 구매자를 선택하지 않고 구매 완료 처리했습니다.");
                    salePost.markAsSold(null); // 구매자 없이 처리
                    return "구매자 없이 판매 완료 처리되었습니다.";
                }

                Member buyer = memberRepository.findById(buyerPk)
                        .orElseThrow(() -> new EntityNotFoundException("해당 구매자가 존재하지 않습니다."));
                salePost.markAsSold(buyer);
                log.info("게시글 [{}]이(가) 구매자 [{}]에 의해 판매 완료 처리되었습니다.", postPk, buyerPk);
                return "구매자 [" + buyer.getNickname() + "]에 의해 판매 완료 처리되었습니다.";

            default:
                throw new IllegalArgumentException("지원하지 않는 상태입니다: " + newStatus);
        }
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

        List<SalePostImage> images =
                IntStream.range(0, imageUrls.size())
                        .mapToObj(i -> SalePostImage.ofWithOrder(salePost, imageUrls.get(i), i))
                        .collect(Collectors.toList());

        salePostImageRepository.saveAll(images);
    }


    /** 작성자 권한 확인 헬퍼 **/
    private void validateWriter(SalePost salePost, Member writer) {
        if (!salePost.getSellerPk().getMemberPk().equals(writer.getMemberPk())) {
            throw new AccessDeniedException("작성자만 수행할 수 있습니다.");
        }
    }


}