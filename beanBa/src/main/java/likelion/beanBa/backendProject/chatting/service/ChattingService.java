package likelion.beanBa.backendProject.chatting.service;

import jakarta.persistence.EntityNotFoundException;
import likelion.beanBa.backendProject.chatting.dto.ChattingRoomListResponse;
import likelion.beanBa.backendProject.chatting.repository.ChattingMessageRepository;
import likelion.beanBa.backendProject.chatting.repository.impl.ChattingRoomCustomImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChattingService {

    private final ChattingMessageRepository chattingMessageRepository;
    private final ChattingRoomCustomImpl chattingRoomCustomImpl;

    /*
     * 상대방이 보낸 메시지 읽음 처리
     * */
    public void messageRead(Long roomPk, Long memberPk) {
        int updatedCount = chattingMessageRepository.messageRead(roomPk, memberPk);
    }

    /*
     * 모든 채팅룸 중 안 읽은 메시지 있는 지 확인(로그인 후 호출 됨)
     * */
    public Map<String, Object> checkReadYn(Long memberPk) {
        List<ChattingRoomListResponse> chattingRoomList = chattingRoomCustomImpl.getUnreadChattingRoomList(memberPk);

        Map<String, Object> checkMap = new HashMap<>();
        if(chattingRoomList.size() > 0) { // 내가 안 읽은 채팅룸 메시지가 있을 시
            checkMap.put("readYn" , "N");
            checkMap.put("unreadCnt", chattingRoomList.size()); // 안 읽은 채팅룸 갯수
        } else { // 내가 안 읽은 채팅룸 메시지가 없을 시
            checkMap.put("readYn" , "Y");
            checkMap.put("unreadCnt", 0);
        }

        return checkMap;
    }

}
