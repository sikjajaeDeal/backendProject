package likelion.beanBa.backendProject.report.repository;

import likelion.beanBa.backendProject.member.Entity.Member;
import likelion.beanBa.backendProject.report.entity.Report;
import likelion.beanBa.backendProject.product.entity.SalePost;
import likelion.beanBa.backendProject.report.entity.ReportKind;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findBySalePost(SalePost salePost);
    Optional<Report> findByReporter(Member reporter);
    List<Report> findByReportee(Member reportee);

    boolean existsByReporterAndReporteeAndSalePost(Member reporter, Member reportee, SalePost salePost);
    long countByReportee_MemberPkAndReportKind(Long reporteePk, ReportKind reportKind);
    long countBySalePost_PostPkAndReportKind(Long postPk, ReportKind reportKind);
}
