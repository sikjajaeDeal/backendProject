package likelion.beanBa.backendProject.chatting.repository;

import likelion.beanBa.backendProject.chatting.dto.ChattingRoomListResponse;

import java.util.List;

/**
 * 채팅룸 리스트 관련 repository interface
 * 구현체는 ./impl/ChattingRoomCustomImpl.java
 * */
public interface ChattingRoomCustom {
    /*
     * 모든 채팅룸 리스트 가져오기
     */
    List<ChattingRoomListResponse> getAllChattingRoomList(Long memberPk);

    /*
     * 특정 상품에 대한 채팅룸 리스트 가져오기 in 상품상세화면
     */
    List<ChattingRoomListResponse> getChattingRoomListByPostPk(Long postPk, Long memberPk);
}
