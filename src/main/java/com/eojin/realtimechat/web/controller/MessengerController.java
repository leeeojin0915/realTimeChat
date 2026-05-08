package com.eojin.realtimechat.web.controller;

import com.eojin.realtimechat.web.domain.entity.messenger.MessengerRoom;
import com.eojin.realtimechat.web.service.MessengerService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/messenger")
@RequiredArgsConstructor
@Slf4j
public class MessengerController {

    private final MessengerService messengerService;

    @GetMapping("/friends/{memberId}")
    public ResponseEntity<?> getFriends(@PathVariable @NonNull Long memberId) {
        return ResponseEntity.ok(messengerService.getFriends(memberId));
    }

    @PostMapping("/friends/{memberId}")
    public ResponseEntity<?> addFriend(@PathVariable @NonNull Long memberId, @RequestBody @NonNull AddFriendRequest request) {
        try {
            log.info("Add friend request: memberId={}, friendUsername={}", memberId, request.getFriendUsername());
            messengerService.addFriend(memberId, request.getFriendUsername());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error adding friend", e);
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/rooms/{memberId}")
    public ResponseEntity<?> getRooms(@PathVariable @NonNull Long memberId) {
        return ResponseEntity.ok(messengerService.getRooms(memberId));
    }

    @PostMapping("/rooms")
    public ResponseEntity<?> createRoom(@RequestBody @NonNull CreateRoomRequest request) {
        try {
            MessengerRoom room = messengerService.createRoom(request.getMemberId(), request.getFriendIds());
            return ResponseEntity.ok(room);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/rooms/{roomId}/messages")
    public ResponseEntity<?> getMessages(@PathVariable @NonNull Long roomId) {
        return ResponseEntity.ok(messengerService.getMessages(roomId));
    }

    @DeleteMapping("/rooms/{roomId}/leave/{memberId}")
    public ResponseEntity<?> leaveRoom(@PathVariable @NonNull Long roomId, @PathVariable @NonNull Long memberId) {
        try {
            messengerService.leaveRoom(roomId, memberId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/members/{memberId}/profile")
    public ResponseEntity<?> updateProfile(@PathVariable @NonNull Long memberId, @RequestBody @NonNull UpdateProfileRequest request) {
        try {
            return ResponseEntity.ok(messengerService.updateProfile(memberId, request.getNickname(), request.getProfileImageUrl()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateProfileRequest {
        private String nickname;
        private String profileImageUrl;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddFriendRequest {
        @NonNull
        private String friendUsername;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateRoomRequest {
        @NonNull
        private Long memberId;
        @NonNull
        private java.util.List<Long> friendIds;
    }
}
