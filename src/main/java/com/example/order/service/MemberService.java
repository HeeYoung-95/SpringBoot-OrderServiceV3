package com.example.order.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.order.model.member.Member;
import com.example.order.model.member.RoleType;
import com.example.order.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberService {

	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;
	

	// 아이디로 회원정보 조회
	public Member findMemberById(String member_id) {
		return memberRepository.findMemberById(member_id);
	}

	// 회원정보 등록
	public void saveMember(Member member) {
		String rawPassword = member.getPassword();
		String encPassword = passwordEncoder.encode(rawPassword);
		member.setPassword(encPassword);
//		log.info("encPassword: {}", encPassword);
		
		// 기본 ROLE 부여
		member.setRole(RoleType.ROLE_USER);
		
		memberRepository.saveMember(member);
	}
}
