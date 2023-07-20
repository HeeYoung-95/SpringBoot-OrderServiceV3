package com.example.order.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.order.config.UserInfo;
import com.example.order.model.member.Member;
import com.example.order.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/*
 * 로그인 폼에서 아이디와 패스워드를 입력하고 로그인 요청을 하면 UserDetailsService에 있는
 * loadUserByUsername 메소드를 자동으로 호출
 */

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService{
	
	private final MemberRepository memberRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.info("loadUserByUsername: {}", username);
		Member member = memberRepository.findMemberById(username);
		if (member != null) {
			return new UserInfo(member);
		}
		throw new UsernameNotFoundException("사용자를 찾을 수 없습니다");
	}

	
}
