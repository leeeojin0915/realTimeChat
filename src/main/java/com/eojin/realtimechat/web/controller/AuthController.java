package com.eojin.realtimechat.web.controller;

import com.eojin.realtimechat.web.domain.entity.messenger.Member;
import com.eojin.realtimechat.web.domain.repository.messenger.MemberRepository;
import com.eojin.realtimechat.web.domain.dto.LoginRequest;
import com.eojin.realtimechat.web.domain.dto.SignupRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@lombok.extern.slf4j.Slf4j
public class AuthController {

    private final MemberRepository memberRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        log.info("Login attempt for username: {}", request.getUsername());
        Optional<Member> memberOpt = memberRepository.findByUsername(request.getUsername());
        
        if (memberOpt.isPresent()) {
            boolean matches = memberOpt.get().getPassword().equals(request.getPassword());
            log.info("User found. Password match: {}", matches);
            if (matches) {
                return ResponseEntity.ok(memberOpt.get());
            }
        } else {
            log.warn("User not found: {}", request.getUsername());
        }
        return ResponseEntity.status(401).body("Invalid credentials");
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest request) {
        if (memberRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username already exists");
        }
        Member newMember = Member.create(request.getUsername(), request.getPassword(), request.getNickname(), request.getProfileImageUrl());
        memberRepository.save(newMember);
        return ResponseEntity.ok(newMember);
    }

}
