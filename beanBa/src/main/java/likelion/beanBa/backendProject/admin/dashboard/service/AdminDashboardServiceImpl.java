package likelion.beanBa.backendProject.admin.dashboard.service;


import likelion.beanBa.backendProject.member.repository.MemberRepository;
import likelion.beanBa.backendProject.product.repository.SalePostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class AdminDashboardServiceImpl implements AdminDashboardService{

    private final MemberRepository memberRepository;
    private final SalePostRepository  salePostRepository;

    @Override
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> result = new HashMap<>();
        result.put("totalMembers", memberRepository.count());
        result.put("totalPosts", salePostRepository.count());
        result.put("newPostsToday", salePostRepository.countByCreatedAtToday());

        return result;
    }
}
