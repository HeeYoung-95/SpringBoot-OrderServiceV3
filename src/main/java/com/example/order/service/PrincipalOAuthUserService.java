package com.example.order.service;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.example.order.config.PrincipalDetails;
import com.example.order.model.member.Member;
import com.example.order.model.member.RoleType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PrincipalOAuthUserService extends DefaultOAuth2UserService {

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		log.info("getClientRegistration: {}", userRequest.getClientRegistration());
		log.info("getAccessToken: {}", userRequest.getAccessToken());
		
		OAuth2User oAuthUser = super.loadUser(userRequest);
		log.info("oAuthUser.getAttributes: {}", oAuthUser.getAttributes());
		
		String email = oAuthUser.getAttribute("email");
		String name = oAuthUser.getAttribute("name");
		
		Member member = new Member();
		member.setMember_id(email);
		member.setName(name);
		member.setRole(RoleType.ROLE_USER);
		
		return new PrincipalDetails(member, oAuthUser.getAttributes());
	}

}
