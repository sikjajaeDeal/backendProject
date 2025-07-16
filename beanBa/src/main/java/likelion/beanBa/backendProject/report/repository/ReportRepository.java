package likelion.beanBa.backendProject.report.repository;

import likelion.beanBa.backendProject.member.Entity.Member;
import likelion.beanBa.backendProject.report.entity.Report;
import likelion.beanBa.backendProject.product.entity.SalePost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Long> {
    Optional<Report> findBySalePost(SalePost salePost);
    Optional<Report> findByReporter(Member reporter);
    Optional<Report> findByReportee(Member reportee);

    boolean existsByReporterAndReporteeAndSalePost(Member reporter, Member reportee, SalePost salePost);
}
