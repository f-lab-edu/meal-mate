package com.flab.mealmate.global.config.security;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.flab.mealmate.domain.model.User;

@Component
public class AuditorAwareConfig implements AuditorAware<User> {

    @Override
    public Optional<User> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }

        // todo : 토큰에서 사용자 정보 가져오기
        Object principal = authentication.getPrincipal();

        // 로그인 기능 개발전까지 systemUser 사용
        return Optional.of(User.createSystemUser());
    }
}

