package likelion.beanBa.backendProject.chatting.controller;


import jakarta.persistence.EntityNotFoundException;

import likelion.beanBa.backendProject.chatting.dto.*;
import likelion.beanBa.backendProject.chatting.handler.StompPrincipal;
import likelion.beanBa.backendProject.chatting.service.ChattingService;
import likelion.beanBa.backendProject.member.security.annotation.CurrentUser;
import likelion.beanBa.backendProject.member.security.service.CustomUserDetails;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;

/**
 * 채팅 관련 controller
 * */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chatting")
@Slf4j
public class ChattingController {

    private final ChattingService chattingService;

    /**
     * 상품 상세화면에서 채팅방 열 때(채팅방 없을 시 생성)
     * return : 채팅방 pk
     */
    @PostMapping("/openChattingRoom")
    public ResponseEntity<?> openChattingRoom(
            @RequestBody ChattingRequest chattingRequest,
            @CurrentUser CustomUserDetails userDetails) {

        try {
            Long memberPk = (userDetails != null && userDetails.getMember() != null)
                    ? userDetails.getMember().getMemberPk()
                    : null;

            OpenChattingRoomResponse openChattingRoomResponse =
                    chattingService.openChattingRoom(chattingRequest.getPostPk(), memberPk);

            return ResponseEntity.ok(openChattingRoomResponse);

        } catch (IllegalArgumentException e) {
            // 파라미터/인증 정보가 유효하지 않은 경우
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (EntityNotFoundException e) {
            // 대상 엔티티가 존재하지 않는 경우
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * 모든 채팅룸 리스트 가져오기
     */
    @GetMapping("/getAllChattingRoomList")
    public ResponseEntity<?> getAllChattingRoomList(@CurrentUser CustomUserDetails userDetails) {
        try {
            Long memberPk = (userDetails != null && userDetails.getMember() != null)
                    ? userDetails.getMember().getMemberPk()
                    : null;

            List<ChattingRoomListResponse> chattingRoomList =
                    chattingService.getAllChattingRoomList(memberPk);

            return ResponseEntity.ok(chattingRoomList);

        } catch (IllegalArgumentException e) {
            // 로그인 정보가 유효하지 않은 경우
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * 특정 상품에 대한 채팅룸 리스트 가져오기 in 상품상세화면
     */
    @GetMapping("/getChattingRoomListByPostPk")
    public ResponseEntity<?> getChattingRoomListByPostPk(@RequestParam("postPk") Long postPk
            , @CurrentUser CustomUserDetails userDetails) {
        try {
            Long memberPk = (userDetails != null && userDetails.getMember() != null)
                    ? userDetails.getMember().getMemberPk()
                    : null;

            List<ChattingRoomListResponse> chattingRoomList =
                    chattingService.getChattingRoomListByPostPk(postPk, memberPk);

            return ResponseEntity.ok(chattingRoomList);

        } catch (IllegalArgumentException e) {
            // 파라미터/인증 정보가 유효하지 않은 경우 400
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    /**
    * DB에 저장되어 있는 과거 채팅 메시지 가져오기
    */
    @GetMapping("/getMessageList")
    public ResponseEntity<?> getMessageList(ChattingRequest chattingRequest) {
        try {
            List<ChattingMessageResponse> responseList =
                    chattingService.getMessageList(chattingRequest.getRoomPk());
            return ResponseEntity.ok(responseList);
        } catch (IllegalArgumentException e) {
            // roomPk가 null인 경우
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (EntityNotFoundException e) {
            // 채팅방이 없는 경우
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * 모든 채팅룸 중 안 읽은 메시지 있는지 확인 (로그인 후 호출됨)
     */
    @GetMapping("/checkReadYn")
    public ResponseEntity<?> checkReadYn(@CurrentUser CustomUserDetails userDetails) {
        Long memberPk = (userDetails != null && userDetails.getMember() != null)
                ? userDetails.getMember().getMemberPk()
                : null;

        ReadYnResponse readYnResponse = chattingService.checkReadYn(memberPk);

        return ResponseEntity.ok(readYnResponse);
    }


    /**
    * 실시간 채팅
    */
    @MessageMapping("/chatting/sendMessage")
    public void sendmessage(ChattingRequest chattingRequest, Principal principal) {
        Long memberPk = (principal instanceof StompPrincipal sp) ? sp.getMemberPk() : null;
        chattingService.sendMessage(chattingRequest, memberPk); // 반환 바디 없음(STOMP 구독 채널로 전달)
    }

    /**
    * 상대방이 보낸 메시지 읽음 처리
    */
    @PatchMapping("/messageRead/{roomPk}")
    public ResponseEntity<?> messageRead(@PathVariable("roomPk") Long roomPk, @CurrentUser CustomUserDetails userDetails){
        // 현 로그인 한 사용자 member pk
        Long memberPk = (userDetails != null && userDetails.getMember() != null)
                ? userDetails.getMember().getMemberPk()
                : null;

        chattingService.messageRead(roomPk, memberPk);
        return ResponseEntity.ok().build();

    }

}
