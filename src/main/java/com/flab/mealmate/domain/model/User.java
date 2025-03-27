package com.flab.mealmate.domain.model;

import java.util.Objects;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User {

	private Long userId;
	private String userName;

	public User(@NonNull Long userId, @NonNull String userName) {
		this.userId = userId;
		this.userName = userName;
	}

	public static User createSystemUser() {
		return new User(0L, "System");
	}

	public boolean equalsUserId(Long userId) {
		return Objects.equals(this.userId, userId);
	}
}
