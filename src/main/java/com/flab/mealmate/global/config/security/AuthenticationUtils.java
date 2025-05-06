package com.flab.mealmate.global.config.security;

import com.flab.mealmate.domain.model.User;

/**
 * 현재 인증된 사용자의 ID를 반환합니다.
 * 현재는 시스템 사용자(User.createSystemUser())로 대체되어 있으며,
 * 향후 SecurityContext 또는 인증 객체에서 실제 사용자 ID를 추출하도록 변경이 필요합니다.
 * @return 사용자 ID (현재는 시스템 사용자 ID)
 */
public class AuthenticationUtils {

	public static Long getUserId() {
		return User.createSystemUser().getUserId();
	}
}
