package likelion.beanBa.backendProject.chatting.repository;

import likelion.beanBa.backendProject.chatting.dto.ChattingRoomListResponse;

import java.util.List;

/**
 * 채팅룸 리스트 관련 repository interface
 * 구현체는 ./impl/ChattingRoomCustomImpl.java
 * */
public interface ChattingRoomCustom {
    /*
    * 채팅룸 리스트 가져오기
    * */
    List<ChattingRoomListResponse> getChattingRoomList(Long memberPk);
}
