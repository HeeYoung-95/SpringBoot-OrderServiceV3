package com.example.order.config;

import java.util.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.order.model.member.Member;

import lombok.Getter;

@Getter
public class UserInfo implements UserDetails{
	
	private Member member;
	
	public UserInfo(Member member) {
		this.member = member;
	}
	

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// 사용자의 권한을 리턴 
		Collection<GrantedAuthority> collect = new ArrayList<>();
		collect.add(new SimpleGrantedAuthority(member.getRole().name()));
		return collect;
	}

	@Override
	public String getPassword() {
		// password를 리턴
		return member.getPassword();
	}

	@Override
	public String getUsername() {
		// id를 리턴
		return member.getMember_id();
	}

	@Override
	public boolean isAccountNonExpired() {
		// 계정의 기한 만료 여부를 리턴
		// non expired 여부를 묻는것이니까 만료가 안 되었으면 true
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// 계정의 잠금 여부를 리턴
		// 비밀번호 n회 이상 틀렸을 시 잠기는 그런 경우를 말함
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// 접속 권한의 만료 여부를 리턴
		return true;
	}

	@Override
	public boolean isEnabled() {
		// 계정 사용 가능 여부
		// 탈퇴한 계정등의 여부
		return true;
	}

}
