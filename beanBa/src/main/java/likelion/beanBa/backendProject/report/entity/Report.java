package likelion.beanBa.backendProject.report.entity;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import likelion.beanBa.backendProject.member.Entity.Member;
import likelion.beanBa.backendProject.product.entity.SalePost;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "report")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="report_pk")
    private Long reportPk;

    @ManyToOne
    @JoinColumn(name = "post_pk")
    private SalePost salePost;

    @ManyToOne
    @JoinColumn(name = "reporter_pk")
    private Member reporter;

    @ManyToOne
    @JoinColumn(name = "reportee_pk")
    private Member reportee;

    @Column(nullable = false)
    private String reportReason;

    @Enumerated(EnumType.STRING)
    @Column(name = "report_kind",nullable = false, length = 1)
    private ReportKind reportKind;

    @CreationTimestamp
    private LocalDateTime reportAt;
}
