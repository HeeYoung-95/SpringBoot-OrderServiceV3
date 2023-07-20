package com.example.order.model.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RoleType {
	
	ROLE_USER("사용자"),
	ROLE_ADMIN("관리자");
	
	private final String description;
	
}
