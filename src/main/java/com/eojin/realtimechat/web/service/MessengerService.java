package com.eojin.realtimechat.web.service;

import com.eojin.realtimechat.web.domain.dto.FriendshipDTO;
import com.eojin.realtimechat.web.domain.dto.MessageResponseDTO;
import com.eojin.realtimechat.web.domain.dto.MessengerRoomDTO;
import com.eojin.realtimechat.web.domain.entity.messenger.*;
import com.eojin.realtimechat.web.domain.repository.messenger.*;
import com.eojin.realtimechat.web.mapper.MessengerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MessengerService {

    private final MemberRepository memberRepository;
    private final FriendshipRepository friendshipRepository;
    private final MessengerRoomRepository roomRepository;
    private final MessengerMemberRepository roomMemberRepository;
    private final MessengerMessageRepository messageRepository;
    private final MessengerMapper messengerMapper;

    // Friends
    public List<FriendshipDTO> getFriends(Long memberId) {
        return friendshipRepository.findByMemberId(memberId).stream()
                .map(fs -> FriendshipDTO.builder()
                        .id(fs.getId())
                        .friend(FriendshipDTO.FriendInfo.builder()
                                .id(fs.getFriend().getId())
                                .username(fs.getFriend().getUsername())
                                .nickname(fs.getFriend().getNickname())
                                .profileImageUrl(fs.getFriend().getProfileImageUrl())
                                .build())
                        .createdAt(fs.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public void addFriend(Long memberId, String friendUsername) {
        Member member = memberRepository.findById(memberId).orElseThrow();
        Member friend = memberRepository.findByUsername(friendUsername).orElseThrow();
        
        if (!friendshipRepository.existsByMember_IdAndFriend_Id(memberId, friend.getId())) {
            friendshipRepository.save(Friendship.create(member, friend));
        }
        if (!friendshipRepository.existsByMember_IdAndFriend_Id(friend.getId(), memberId)) {
            friendshipRepository.save(Friendship.create(friend, member));
        }
    }

    // Rooms
    public List<MessengerRoomDTO> getRooms(Long memberId) {
        return messengerMapper.findRoomsByMemberId(memberId);
    }

    @Transactional
    public MessengerRoom createRoom(Long memberId, List<Long> friendIds) {
        // 1:1 대화방의 경우 기존 방이 있는지 확인
        if (friendIds.size() == 1) {
            Long friendId = friendIds.get(0);
            Long existingRoomId = messengerMapper.findExistingRoom(memberId, friendId);
            if (existingRoomId != null) {
                return roomRepository.findById(existingRoomId).orElseThrow();
            }
        }

        Member member = memberRepository.findById(memberId).orElseThrow();
        List<Member> participants = friendIds.stream()
                .map(id -> memberRepository.findById(id).orElseThrow())
                .collect(Collectors.toList());

        // 방 이름 생성 (참여자 닉네임 조합)
        String roomName = member.getNickname() + ", " + participants.stream()
                .map(Member::getNickname)
                .collect(Collectors.joining(", "));
        
        if (roomName.length() > 50) roomName = roomName.substring(0, 47) + "...";

        MessengerRoom room = MessengerRoom.create(roomName);
        roomRepository.save(room);

        // 멤버 추가
        roomMemberRepository.save(MessengerMember.create(room, member));
        for (Member p : participants) {
            if (!p.getId().equals(memberId)) {
                roomMemberRepository.save(MessengerMember.create(room, p));
            }
        }

        return room;
    }

    // Messages
    public List<MessageResponseDTO> getMessages(Long roomId) {
        return messageRepository.findByRoomIdOrderBySentAtAsc(roomId).stream()
                .map(msg -> MessageResponseDTO.builder()
                        .id(msg.getId())
                        .roomId(msg.getRoom().getId())
                        .senderId(msg.getSender().getId())
                        .senderNickname(msg.getSender().getNickname())
                        .senderProfileUrl(msg.getSender().getProfileImageUrl())
                        .content(msg.getContent())
                        .fileUrl(msg.getFileUrl())
                        .messageType(msg.getMessageType().name())
                        .sentAt(msg.getSentAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public MessageResponseDTO saveMessage(Long roomId, Long senderId, String content, String fileUrl, MessageType type) {
        MessengerRoom room = roomRepository.findById(roomId).orElseThrow();
        Member sender = memberRepository.findById(senderId).orElseThrow();

        MessengerMessage msg;
        if (type == MessageType.TEXT) {
            msg = MessengerMessage.createText(room, sender, content);
        } else {
            msg = MessengerMessage.createFile(room, sender, content, fileUrl, type);
        }
        messageRepository.save(msg);

        return MessageResponseDTO.builder()
                .id(msg.getId())
                .roomId(room.getId())
                .senderId(sender.getId())
                .senderNickname(sender.getNickname())
                .senderProfileUrl(sender.getProfileImageUrl())
                .content(msg.getContent())
                .fileUrl(msg.getFileUrl())
                .messageType(msg.getMessageType().name())
                .sentAt(msg.getSentAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .build();
    }

    @Transactional
    public void leaveRoom(Long roomId, Long memberId) {
        roomMemberRepository.deleteByRoomIdAndMemberId(roomId, memberId);
        
        // Optional: If no members left, delete messages and room
        List<MessengerMember> remaining = roomMemberRepository.findByRoomId(roomId);
        if (remaining.isEmpty()) {
            messageRepository.deleteByRoomId(roomId);
            roomRepository.deleteById(roomId);
        }
    }

    @Transactional
    public Member updateProfile(Long memberId, String nickname, String profileImageUrl) {
        Member member = memberRepository.findById(memberId).orElseThrow();
        member.updateProfile(nickname, profileImageUrl);
        return member;
    }
}
