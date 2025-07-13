package likelion.beanBa.backendProject.chatting.repository;

import likelion.beanBa.backendProject.chatting.entity.ChattingRoom;
import likelion.beanBa.backendProject.member.Entity.Member;
import likelion.beanBa.backendProject.product.entity.SalePost;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface ChattingRoomRepository extends JpaRepository<ChattingRoom,Long> {

    /*
     * 등록상품pk와 멤버pk로 채팅룸 가져오기
     * */
    Optional<ChattingRoom> findBySalePostAndBuyMember(SalePost salePost, Member buyMember);

}
