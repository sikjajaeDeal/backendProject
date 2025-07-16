package likelion.beanBa.backendProject.report.service;

import likelion.beanBa.backendProject.member.Entity.Member;
import likelion.beanBa.backendProject.report.dto.ReportRequest;
import likelion.beanBa.backendProject.report.entity.Report;
import likelion.beanBa.backendProject.report.repository.ReportRepository;
import likelion.beanBa.backendProject.member.repository.MemberRepository;
import likelion.beanBa.backendProject.product.entity.SalePost;
import likelion.beanBa.backendProject.product.repository.SalePostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final MemberRepository memberRepository;
    private final SalePostRepository salePostRepository;

    public void reportUser(ReportRequest request, Member reporter) {

        SalePost post = salePostRepository.findById(request.getPostPk())
                .orElseThrow(() -> new IllegalArgumentException("해당 글이 없습니다."));

        Member reportee = memberRepository.findByMemberId(request.getReporteeId())
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다."));

        Report report = Report.builder()
                .salePost(post)
                .reporter(reporter)
                .reportee(reportee)
                .reportReason(request.getReportReason())
                .reportKind(request.getReportKind())
                .build();
        reportRepository.save(report);
    }

    public void blindPost(SalePost salePost){
        SalePost post = salePostRepository.findById(salePost.getPostPk())
                .orElseThrow(() -> new IllegalArgumentException("해당 글이 없습니다."));
    }
}
