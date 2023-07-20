package com.example.order.config;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CustomAuthFailureHandler extends SimpleUrlAuthenticationFailureHandler{
	// 로그인이 실패 했을 때 어떤 이유로 실패했는지 상세하게 보고싶을때

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		log.info("실패 핸들러 호출: {}", exception);
		String errorMessage;
		if (exception instanceof BadCredentialsException) {
			errorMessage = "아이디 또는 패스워드가 맞지 않습니다.";
		} else if (exception instanceof LockedException) {
			errorMessage = "잠겨있는 계정입니다.";
		} else {
			errorMessage = "알 수 없는 오류로 로그인에 실패했습니다.";
		}
		errorMessage = URLEncoder.encode(errorMessage, "UTF-8");
		setDefaultFailureUrl("/member/login?error=true&message=" + errorMessage);
		super.onAuthenticationFailure(request, response, exception);
	}
	
}
