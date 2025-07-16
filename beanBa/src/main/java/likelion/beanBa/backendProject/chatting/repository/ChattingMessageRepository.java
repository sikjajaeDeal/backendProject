package likelion.beanBa.backendProject.chatting.repository;

import likelion.beanBa.backendProject.chatting.entity.ChattingMessage;
import likelion.beanBa.backendProject.chatting.entity.ChattingRoom;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChattingMessageRepository extends JpaRepository<ChattingMessage,Long> {

    /*
    * 기존 특정 채팅방에 존재하는 메시지 내용 가져오기
    * */
    Optional<List<ChattingMessage>> findByChattingRoomOrderByIdAsc(ChattingRoom chattingRoom);

}
