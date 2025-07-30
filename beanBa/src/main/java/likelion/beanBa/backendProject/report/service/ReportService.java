package likelion.beanBa.backendProject.report.service;

import likelion.beanBa.backendProject.member.Entity.Member;
import likelion.beanBa.backendProject.product.product_enum.Yn;
import likelion.beanBa.backendProject.report.dto.ReportRequest;
import likelion.beanBa.backendProject.report.entity.Report;
import likelion.beanBa.backendProject.report.entity.ReportKind;
import likelion.beanBa.backendProject.report.repository.ReportRepository;
import likelion.beanBa.backendProject.member.repository.MemberRepository;
import likelion.beanBa.backendProject.product.entity.SalePost;
import likelion.beanBa.backendProject.product.repository.SalePostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final MemberRepository memberRepository;
    private final SalePostRepository salePostRepository;

    public void createReport(ReportRequest request, Member reporter) {

        // 조회 시 예외 처리
        SalePost post = salePostRepository.findById(request.getPostPk())
                .orElseThrow(() -> new IllegalArgumentException("해당 글이 없습니다."));
        Member reportee = memberRepository.findByMemberPk(request.getReporteePk())
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다."));

        if(reporter.getMemberPk().equals(post.getSellerPk().getMemberPk())) {
            request.setReportKind(ReportKind.BUYER);
        } else {
            request.setReportKind(ReportKind.SELLER);
        }

        // report 검증
        if(reportRepository.existsByReporterAndReporteeAndSalePost(reporter, reportee, post)) {
            throw new IllegalArgumentException("이미 존재하는 report 입니다.");
        }

        // report 저장
        Report report = request.toEntity(post, reporter, reportee);
        reportRepository.save(report);

        // 후처리
        ReportKind kind = request.getReportKind();
        switch (kind) {
            case BUYER -> blindMember(reportee);
            case SELLER -> blindPost(post, reportee);
        }
    }

    private void blindMember(Member reportee) { // 구매자 신고
        long cnt = reportRepository
                .countByReportee_MemberPkAndReportKind(reportee.getMemberPk(), ReportKind.BUYER);
        if(cnt>=5) {
            reportee.markAsBlind();
        }
    }

    private void blindPost(SalePost salePost, Member reportee){ //판매자 신고
        long sellerCnt = reportRepository
                .countBySalePost_PostPkAndReportKind(salePost.getPostPk(), ReportKind.SELLER);
        if(sellerCnt>=5){
            salePost.markAsBlind();
        }

        long blindPostCnt = salePostRepository.countBySellerPk_MemberPkAndDeleteYn(reportee.getMemberPk(), Yn.B);
        if(blindPostCnt>=5){
            reportee.markAsBlind();
        }
    }

    public List<Member> getAllBlockMember() {
        return memberRepository.findByDeleteYn("B");
    }

    public List<Report> getBlockMember(Long memberPk) {
        Member blockMember = memberRepository.findByMemberPk(memberPk)
                .orElseThrow();
        return reportRepository.findByReportee(blockMember);
    }

    public List<SalePost> getAllBlockPost() {
        return salePostRepository.findAllByDeleteYn(Yn.B);
    }

    public List<Report> getBlockPost(Long postPk) {
        SalePost blockPost = salePostRepository.findById(postPk)
                .orElseThrow();
        return reportRepository.findBySalePost(blockPost);
    }
}
