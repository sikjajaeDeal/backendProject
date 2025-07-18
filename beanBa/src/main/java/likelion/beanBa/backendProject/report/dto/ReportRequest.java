package likelion.beanBa.backendProject.report.dto;

import likelion.beanBa.backendProject.member.Entity.Member;
import likelion.beanBa.backendProject.product.entity.SalePost;
import likelion.beanBa.backendProject.report.entity.Report;
import likelion.beanBa.backendProject.report.entity.ReportKind;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportRequest {
    private Long postPk;
    private String reporteeId;
    private String reportReason;
    private String reportKind;

    public Report toEntity(SalePost post, Member reporter, Member reportee) {
        return Report.builder()
                .salePost(post)
                .reporter(reporter)
                .reportee(reportee)
                .reportReason(this.reportReason)
                .reportKind(ReportKind.of(this.reportKind))
                .build();
    }
}
