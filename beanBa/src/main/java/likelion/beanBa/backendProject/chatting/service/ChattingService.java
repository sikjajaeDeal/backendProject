package likelion.beanBa.backendProject.chatting.service;

import jakarta.persistence.EntityNotFoundException;
import likelion.beanBa.backendProject.chatting.repository.ChattingMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChattingService {

    private final ChattingMessageRepository chattingMessageRepository;

    /*
     * 상대방이 보낸 메시지 읽음 처리
     * */
    public void messageRead(Long roomPk, Long memberPk) {
        int updatedCount = chattingMessageRepository.messageRead(roomPk, memberPk);
    }

}
