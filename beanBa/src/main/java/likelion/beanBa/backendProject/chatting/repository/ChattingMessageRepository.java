package likelion.beanBa.backendProject.chatting.repository;

import likelion.beanBa.backendProject.chatting.entity.ChattingMessage;
import likelion.beanBa.backendProject.chatting.entity.ChattingRoom;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChattingMessageRepository extends JpaRepository<ChattingMessage,Long> {

    /*
    * 기존 특정 채팅방에 존재하는 메시지 내용 가져오기
    * */
    Optional<List<ChattingMessage>> findByChattingRoomOrderByIdAsc(ChattingRoom chattingRoom);

    /*
     * 상대방이 보낸 메시지 읽음 처리
     * */
    @Modifying
    @Transactional
    @Query("UPDATE ChattingMessage m SET m.readYn = 'Y' WHERE m.chattingRoom.id = :roomPk AND m.member.id != :memberPk")
    int messageRead(@Param("roomPk") Long roomPk, @Param("memberPk") Long memberPk);

}
