package likelion.beanBa.backendProject.chatting.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import likelion.beanBa.backendProject.chatting.dto.*;
import likelion.beanBa.backendProject.chatting.entity.ChattingMessage;
import likelion.beanBa.backendProject.chatting.entity.ChattingRoom;
import likelion.beanBa.backendProject.chatting.repository.ChattingMessageRepository;
import likelion.beanBa.backendProject.chatting.repository.ChattingRoomRepository;
import likelion.beanBa.backendProject.chatting.repository.impl.ChattingRoomCustomImpl;
import likelion.beanBa.backendProject.member.Entity.Member;
import likelion.beanBa.backendProject.member.repository.MemberRepository;
import likelion.beanBa.backendProject.product.entity.SalePost;
import likelion.beanBa.backendProject.product.repository.SalePostRepository;
import likelion.beanBa.backendProject.redis.RedisPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ChattingService {

    // redisPublisher
    private final RedisPublisher redisPublisher;

    // json<->dto 를 위한 객체
    private final ObjectMapper objectMapper;

    private final ChattingRoomRepository chattingRoomRepository;
    private final ChattingMessageRepository chattingMessageRepository;
    private final ChattingRoomCustomImpl chattingRoomCustomImpl;

    private final MemberRepository memberRepository;
    private final SalePostRepository salePostRepository;

    /**
     * 상품 상세화면에서 채팅방 열기 (없으면 생성)
     * @param postPk   상품(게시글) PK (필수)
     * @param memberPk 현재 로그인 사용자 PK (필수)
     * @return OpenChattingRoomResponse
     * @throws IllegalArgumentException 파라미터가 유효하지 않을 때
     * @throws jakarta.persistence.EntityNotFoundException 대상 엔티티가 없을 때
     */
    @Transactional
    public OpenChattingRoomResponse openChattingRoom(Long postPk, Long memberPk) {
        if (memberPk == null) {
            throw new IllegalArgumentException("로그인을 다시 확인해 주시기 바랍니다.");
        }
        if (postPk == null) {
            throw new IllegalArgumentException("해당 상품을 찾을 수 없습니다. 새로고침 후 다시 시도해 주시기 바랍니다.");
        }

        SalePost salePost = salePostRepository.findById(postPk)
                .orElseThrow(() -> new EntityNotFoundException("해당 상품을 찾을 수 없습니다. 새로고침 후 다시 시도해 주시기 바랍니다."));

        Member buyMember = memberRepository.findById(memberPk)
                .orElseThrow(() -> new EntityNotFoundException("로그인을 다시 확인해 주시기 바랍니다."));

        ChattingRoom chattingRoom = chattingRoomRepository
                .findBySalePostAndBuyMember(salePost, buyMember) // 기존에 채팅방이 존재할 시
                .orElseGet(() -> { // 기존에 채팅방이 존재하지 않을 시 새로운 채팅방 만들기
                    ChattingRoom newRoom = new ChattingRoom();
                    newRoom.setSalePost(salePost);
                    newRoom.setBuyMember(buyMember);
                    return chattingRoomRepository.save(newRoom);
                });

        OpenChattingRoomResponse openChattingRoomResponse = OpenChattingRoomResponse.builder()
                .roomPk(chattingRoom.getId()) // 상품 상세화면에서 사용 될 채팅방의 pk값
                .memberPk(memberPk) // 현재 로그인 사용자 pk값
                .build();

        return openChattingRoomResponse;
    }

    /**
     * 모든 채팅룸 리스트 가져오기 (현재 로그인 사용자 기준)
     *
     * @param memberPk 현재 로그인 사용자 PK (필수)
     * @return 채팅룸 리스트
     * @throws IllegalArgumentException memberPk가 null일 때
     */
    public List<ChattingRoomListResponse> getAllChattingRoomList(Long memberPk) {
        if (memberPk == null) {
            throw new IllegalArgumentException("로그인을 다시 확인해 주시기 바랍니다.");
        }

        List<ChattingRoomListResponse> chattingRoomList =
                chattingRoomCustomImpl.getAllChattingRoomList(memberPk);

        return chattingRoomList != null ? chattingRoomList : Collections.emptyList();
    }


    /**
     * 특정 상품의 채팅룸 리스트 조회 (상품 상세 화면)
     *
     * @param postPk   게시글 PK (필수)
     * @param memberPk 현재 로그인 사용자 PK (필수)
     * @return 채팅룸 리스트
     * @throws IllegalArgumentException 파라미터가 유효하지 않을 때
     */
    public List<ChattingRoomListResponse> getChattingRoomListByPostPk(Long postPk, Long memberPk) {
        if (memberPk == null) {
            throw new IllegalArgumentException("로그인을 다시 확인해 주시기 바랍니다.");
        }
        if (postPk == null) {
            throw new IllegalArgumentException("새로고침 후 다시 시도해 주시기 바랍니다.");
        }

        List<ChattingRoomListResponse> chattingRoomList =
                chattingRoomCustomImpl.getChattingRoomListByPostPk(postPk, memberPk);

        // null-safe (쿼리에서 null이 올 가능성 방지)
        return chattingRoomList != null ? chattingRoomList : Collections.emptyList();
    }

    /**
     * DB에 저장되어 있는 과거 채팅 메시지 가져오기
     * @param roomPk 채팅방 PK (필수)
     * @return 채팅 메시지 응답 리스트
     * @throws IllegalArgumentException roomPk가 null일 때
     * @throws EntityNotFoundException 해당 채팅방이 없을 때
     */
    public List<ChattingMessageResponse> getMessageList(Long roomPk) {
        if (roomPk == null) {
            throw new IllegalArgumentException("새로고침 후 다시 채팅방에 접속해 주시기 바랍니다.");
        }

        ChattingRoom chattingRoom = chattingRoomRepository.findById(roomPk)
                .orElseThrow(() -> new EntityNotFoundException("해당하는 채팅방이 존재하지 않습니다. 새로고침 후 다시 채팅방에 접속해 주시기 바랍니다."));

        List<ChattingMessage> messageList = chattingMessageRepository
                .findByChattingRoomOrderByIdAsc(chattingRoom)
                .orElseGet(ArrayList::new);

        return messageList.stream()
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
    }


    /**
     * 상대방이 보낸 메시지 읽음 처리
     * @param roomPk  채팅방 PK (필수)
     * @param memberPk 현재 로그인 사용자 PK (필수)
     * @throws IllegalArgumentException 파라미터 누락/유효성 실패
     * @throws jakarta.persistence.EntityNotFoundException 채팅방이 없을 때
     */
    @Transactional
    public void messageRead(Long roomPk, Long memberPk) {
        if (memberPk == null) {
            throw new IllegalArgumentException("로그인을 다시 확인해 주시기 바랍니다.");
        }
        if (roomPk == null) {
            throw new IllegalArgumentException("채팅방 정보가 없습니다. 새로고침 후 다시 시도해 주시기 바랍니다.");
        }
        // 방 존재 검증 (선택이지만 안전성을 위해 권장)
        if (!chattingRoomRepository.existsById(roomPk)) {
            throw new EntityNotFoundException("해당 채팅방이 존재하지 않습니다.");
        }

        // 실제 읽음 처리 (영향받은 행 수는 필요시 반환값으로 사용 가능)
        chattingMessageRepository.messageRead(roomPk, memberPk);
    }


    /**
     * 모든 채팅룸 중 안 읽은 메시지가 있는지 확인 (로그인 후 호출됨)
     *
     * @param memberPk 현재 로그인 사용자 PK (필수)
     * @return readYn("Y" or "N"), unreadCnt(안 읽은 채팅방 수)
     * @throws IllegalArgumentException memberPk가 null일 때
     */
    public ReadYnResponse checkReadYn(Long memberPk) {
        if (memberPk == null) {
            throw new IllegalArgumentException("로그인을 다시 확인해 주시기 바랍니다.");
        }

        // 안 읽은 메시지가 존재하는 채팅 룸 리스트
        List<ChattingRoomListResponse> chattingRoomList =
                chattingRoomCustomImpl.getUnreadChattingRoomList(memberPk);

        ReadYnResponse readYnResponse;

        if (chattingRoomList != null && !chattingRoomList.isEmpty()) { // 안 읽은 채팅 메시지가 있을 경우
            readYnResponse = ReadYnResponse.builder()
                    .unReadYn(true)
                    .unreadCnt(chattingRoomList.size())
                    .build();
        } else { // 모든 채팅 메시지를 읽었을 경우
            readYnResponse = ReadYnResponse.builder()
                    .unReadYn(false)
                    .unreadCnt(0)
                    .build();
        }

        return readYnResponse;
    }

    /**
     * 1) 송신자 정보/시간 세팅
     * 2) Redis Publish
     * 3) DB 저장
     *
     * 예외 매핑:
     *  - IllegalArgumentException: 파라미터 누락/유효성 실패
     *  - EntityNotFoundException: 방/회원 등 엔티티 미존재
     *  - IllegalStateException: 직렬화/발행 등 전송 단계 오류
     */
    @Transactional
    public void sendMessage(ChattingRequest chattingRequest, Long memberPk) {
        if (memberPk == null) {
            throw new IllegalArgumentException("로그인을 다시 확인해 주시기 바랍니다.");
        }
        if (chattingRequest == null || chattingRequest.getRoomPk() == null) {
            throw new IllegalArgumentException("채팅방 정보가 없습니다. 새로고침 후 다시 시도해 주시기 바랍니다.");
        }

        // 1) 송신자 정보/시간 세팅
        Member sender = memberRepository.findById(memberPk)
                .orElseThrow(() -> new EntityNotFoundException("로그인을 다시 확인해 주시기 바랍니다."));
        chattingRequest.setFrom(sender.getNickname());
        chattingRequest.setMemberPkFrom(memberPk);

        ZoneId seoul = ZoneId.of("Asia/Seoul");
        chattingRequest.setMessageAt(LocalDateTime.ofInstant(Instant.now(), seoul));

        // 2) Redis Publish
        String channel = "room." + chattingRequest.getRoomPk();
        try {
            String payload = objectMapper.writeValueAsString(chattingRequest);
            redisPublisher.publish(channel, payload);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("메시지 전송에 실패했습니다. 잠시 후 다시 시도해 주세요.", e);
        }

        // 3) DB 저장
        saveChatMessage(chattingRequest, sender);
    }

    /*
    * DB 저장
    */
    private void saveChatMessage(ChattingRequest chattingRequest, Member sender) {
        final String errorMessage = "채팅 전송 중 에러가 발생하였습니다. 새로 고침 후 다시 시도해 주시기 바랍니다.";

        ChattingRoom chattingRoom = chattingRoomRepository.findById(chattingRequest.getRoomPk())
                .orElseThrow(() -> new EntityNotFoundException(errorMessage));

        ChattingMessage chattingMessage = ChattingMessage.builder()
                .message(chattingRequest.getMessage())
                .member(sender)
                .chattingRoom(chattingRoom)
                .messageAt(chattingRequest.getMessageAt())
                .build();

        chattingMessageRepository.save(chattingMessage);
    }

}
