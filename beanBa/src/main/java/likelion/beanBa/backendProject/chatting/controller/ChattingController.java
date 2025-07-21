package likelion.beanBa.backendProject.chatting.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import likelion.beanBa.backendProject.chatting.dto.ChattingRequest;
import likelion.beanBa.backendProject.chatting.dto.ChattingMessageResponse;
import likelion.beanBa.backendProject.chatting.dto.ChattingRoomListResponse;
import likelion.beanBa.backendProject.chatting.entity.ChattingMessage;
import likelion.beanBa.backendProject.chatting.entity.ChattingRoom;
import likelion.beanBa.backendProject.chatting.handler.StompPrincipal;
import likelion.beanBa.backendProject.chatting.repository.ChattingMessageRepository;
import likelion.beanBa.backendProject.chatting.repository.ChattingRoomRepository;
import likelion.beanBa.backendProject.chatting.repository.impl.ChattingRoomCustomImpl;
import likelion.beanBa.backendProject.chatting.service.ChattingService;
import likelion.beanBa.backendProject.member.Entity.Member;
import likelion.beanBa.backendProject.member.repository.MemberRepository;
import likelion.beanBa.backendProject.member.security.annotation.CurrentUser;
import likelion.beanBa.backendProject.member.security.service.CustomUserDetails;
import likelion.beanBa.backendProject.product.entity.SalePost;
import likelion.beanBa.backendProject.product.repository.SalePostRepository;
import likelion.beanBa.backendProject.redis.RedisPublisher;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 채팅 관련 controller
 * */
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/chatting")
@Slf4j
public class ChattingController {

    // redisPublisher
    private final RedisPublisher redisPublisher;

    // json<->dto 를 위한 객체
    private final ObjectMapper objectMapper;

    private final ChattingRoomRepository chattingRoomRepository;
    private final ChattingMessageRepository chattingMessageRepository;
    private final ChattingRoomCustomImpl chattingRoomCustomImpl;

    private final MemberRepository memberRepository;
    private final SalePostRepository salePostRepository;

    private final ChattingService chattingService;
    /*
     *  상품 상세화면에서 채팅방 열 때
     *  return : 채팅방 pk
     * */
    @PostMapping("/openChattingRoom")
    @ResponseBody
    public ResponseEntity<?> openChattingRoom(@RequestBody ChattingRequest chattingRequest,
    @CurrentUser CustomUserDetails userDetails) {
        Long memberPk = userDetails.getMember().getMemberPk(); // 현 로그인 한 사용자 member pk

        // jwt에서 가져온 로그인 정보가 옳지 않은 경우
        if (memberPk == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("로그인을 다시 확인해 주시기 바랍니다.");
        }

        Long postPk = chattingRequest.getPostPk();
        if (postPk == null) { // 클라이언트에서 가져온 postPk가 존재하지 않는 경우
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("해당 상품을 찾을 수 없습니다. 새로고침 후 다시 시도해 주시기 바랍니다.");
        }

        SalePost salePost = salePostRepository.findById(postPk).orElse(null);
        // 클라이언트에서 가져온 postPk로 조회 한 상품등록이 존재하지 않는 경우
        if (salePost == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("해당 상품을 찾을 수 없습니다. 새로고침 후 다시 시도해 주시기 바랍니다.");
        }

        Member buyMember = memberRepository.findById(memberPk).orElse(null);
        if (buyMember == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("로그인을 다시 확인해 주시기 바랍니다.");
        }

        ChattingRoom chattingRoom = chattingRoomRepository.findBySalePostAndBuyMember(salePost, buyMember).orElseGet(() -> {
            ChattingRoom newRoom = new ChattingRoom(); // 기존에 채팅방이 존재하지 않을 시 새로운 채팅방 만들기
            newRoom.setSalePost(salePost);
            newRoom.setBuyMember(buyMember);
            return chattingRoomRepository.save(newRoom);
        });

//        return ResponseEntity.ok(chattingRoom.getId());
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("roomPk", chattingRoom.getId());
        returnMap.put("memberPk", memberPk);

        return ResponseEntity.ok(returnMap);
    }

    /*
    * 모든 채팅룸 리스트 가져오기
    * */
    @GetMapping("/getAllChattingRoomList")
    @ResponseBody
    public ResponseEntity<?> getAllChattingRoomList(@CurrentUser CustomUserDetails userDetails) {
        Long memberPk = userDetails.getMember().getMemberPk(); // 현 로그인 한 사용자 member pk

        // jwt에서 가져온 로그인 정보가 옳지 않은 경우
        if (memberPk == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("로그인을 다시 확인해 주시기 바랍니다.");
        }

        List<ChattingRoomListResponse> chattingRoomList = chattingRoomCustomImpl.getAllChattingRoomList(memberPk);

        return ResponseEntity.ok(chattingRoomList);
    }

    /*
     * 특정 상품에 대한 채팅룸 리스트 가져오기 in 상품상세화면
     */
    @GetMapping("/getChattingRoomListByPostPk")
    @ResponseBody
    public ResponseEntity<?> getChattingRoomListByPostPk(@RequestParam("postPk") Long postPk
            , @CurrentUser CustomUserDetails userDetails) {
        Long memberPk = userDetails.getMember().getMemberPk(); // 현 로그인 한 사용자 member pk

        // jwt에서 가져온 로그인 정보가 옳지 않은 경우
        if (memberPk == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("로그인을 다시 확인해 주시기 바랍니다.");
        }

        if (postPk == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("새로고침 후 다시 시도해 주시기 바랍니다.");
        }

        List<ChattingRoomListResponse> chattingRoomList = chattingRoomCustomImpl.getChattingRoomListByPostPk(postPk, memberPk);

        return ResponseEntity.ok(chattingRoomList);
    }


    /*
    * DB에 저장되어 있는 과거 채팅 메시지 가져오기
    */
    @GetMapping("/getMessageList")
    @ResponseBody
    public ResponseEntity<?> getMessageList(ChattingRequest chattingRequest) {
        Long roomPk = chattingRequest.getRoomPk();

        // roomPk값이 없을 때 400 bad request&메시지 반환
        if(roomPk == null) {
            return ResponseEntity
                    .badRequest()
                    .body("새로고침 후 다시 채팅방에 접속해 주시기 바랍니다.");
        }

        ChattingRoom chattingRoom = chattingRoomRepository.findById(roomPk).orElse(null);

        if (chattingRoom == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("해당하는 채팅방이 존재하지 않습니다. 새로고침 후 다시 채팅방에 접속해 주시기 바랍니다.");
        }

        List<ChattingMessage> messageList = chattingMessageRepository.findByChattingRoomOrderByIdAsc(chattingRoom)
                .orElseGet(ArrayList::new);

        // ChattingMessage → ChattingResponse로 변환
        List<ChattingMessageResponse> responseList = messageList.stream()
                .map(message -> {
                    ChattingRoom room = message.getChattingRoom();
                    Member member = message.getMember();

                    return new ChattingMessageResponse(
                            message.getId(),
                            room != null ? room.getId() : null,
                            member != null ? member.getMemberPk() : null,
                            member != null ? member.getNickname() : null,
                            message.getMessage(),
                            message.getMessageAt()
                    );
                })
                .toList();

        return ResponseEntity.ok(responseList);
    }

    /*
    * (방이 없을 시)동적으로 방 생성 및 채팅
    */
    @MessageMapping("/chatting/sendMessage")
    public void sendmessage(ChattingRequest chattingRequest
//            , @CurrentUser CustomUserDetails userDetails // 웹소켓 통신은 @CurrentUser사용불가
              , Principal principal
            ) throws JsonProcessingException {

        // 테스트용
        /*
        message.setRoomPk(5L);
        message.setPostPk(28L);
        message.setFrom("테스트발신자");
        message.setMemberPkFrom(1L);
        */

        String channel = null;
        String msg = null;
        Long memberPk = ((StompPrincipal) principal).getMemberPk(); // 현 로그인 한 사용자 member pk
        String nickName = null; // 현 로그인 한 사용자 member 닉네임
        Member memberFrom = memberRepository.findByMemberPk(memberPk).orElse(null);
        if(memberFrom != null) {
            nickName = memberFrom.getNickname();
        }

        chattingRequest.setFrom(nickName);
        chattingRequest.setMemberPkFrom(memberPk);

        LocalDateTime nowTime = LocalDateTime.now();
        chattingRequest.setMessageAt(nowTime); // 전송시간 세팅

        // 일반 메시지
        channel = "room."+chattingRequest.getRoomPk();
//        channel = "room.5"; // 테스트용
        msg = objectMapper.writeValueAsString(chattingRequest);

        redisPublisher.publish(channel, msg);

        try {
            // DB에 메시지 저장
            saveMessage(chattingRequest);
        } catch (RuntimeException e) {
            log.error("전송 메시지 mysql 저장 실패 : {}", e.getMessage());

            throw e;
        }

    }

    /*
    * 상대방이 보낸 메시지 읽음 처리
    * */
    @PatchMapping("/messageRead/{roomPk}")
    public ResponseEntity<?> messageRead(@PathVariable("roomPk") Long roomPk, @CurrentUser CustomUserDetails userDetails){
        Long memberPk = userDetails.getMember().getMemberPk(); // 현 로그인 한 사용자 member pk

        chattingService.messageRead(roomPk, memberPk);
        return ResponseEntity.ok().build();

    }

    /*
     *  DB에 메시지 저장
     **/
    private void saveMessage(ChattingRequest chattingRequest) {
        Long roomPk = chattingRequest.getRoomPk(); // 대화 중인 chatting room pk
        String errorMessage = "채팅 전송 중 에러가 발생하였습니다. 새로 고침 후 다시 시도해 주시기 바랍니다.";

        // 메시지 저장 할 때 fk로 쓰일 채팅룸 객체 가져오기
        ChattingRoom chattingRoom = chattingRoomRepository.findById(roomPk).orElseThrow(() ->
                 new RuntimeException(errorMessage));

        // 메시지 저장 할 때 fk로 쓰일 멤버 객체 가져오기
        Long memberPkFrom = chattingRequest.getMemberPkFrom();
        Member member = memberRepository.findById(memberPkFrom).orElseThrow(()
                -> new RuntimeException(errorMessage));

        ChattingMessage chattingMessage = new ChattingMessage();
        chattingMessage.setMessage(chattingRequest.getMessage());
        chattingMessage.setMember(member);
        chattingMessage.setChattingRoom(chattingRoom);
        chattingMessage.setMessageAt(chattingRequest.getMessageAt());

        chattingMessageRepository.save(chattingMessage);
    }

}
