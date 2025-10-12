package likelion.beanBa.backendProject.chatting.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/*
* 모든 채팅룸 중 안 읽은 메시지가 있는지 확인 하는 서비스 로직(checkReadYn)에서 쓰이는 dto
* */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReadYnResponse {

    /*
    * 현재 로그인 한 사용자가 안 읽은 메시지가 있는 지 유무
    * true : 안 읽은 메시지 있음, false : 안 읽은 메시지 없음
    * */
    private boolean unReadYn;

    // 현재 로그인 한 사용자의 안 읽은 메시지가 존재하는 채팅룸 갯수
    private int unreadCnt;

}
