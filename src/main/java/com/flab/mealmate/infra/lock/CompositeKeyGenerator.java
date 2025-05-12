package com.flab.mealmate.infra.lock;


import com.flab.mealmate.global.error.exception.CustomIllegalArgumentException;
import com.flab.mealmate.global.error.exception.ErrorCode;

/**
 * 다양한 목적(Redis/Mysql Lock 등)으로 사용할 수 있는
 * 도메인-식별자 조합 키 생성 유틸리티 클래스.
 *
 * 예: stock-123:item-456
 */
public class CompositeKeyGenerator {

	private CompositeKeyGenerator() {
	}

	private static final String PREFIX = "LOCK:";
	private static final String DOMAIN_SEPARATOR = "-";
	private static final String PAIR_SEPARATOR = ":";

	/**
	 * 파라미터 이름과 값으로 구성된 키를 반환합니다.
	 *
	 * @param keys     key 배열
	 * @param values     값 배열
	 * @return 복합 키 문자열 (예: stock-123:item-456)
	 */
	public static String buildKey(String[] keys, Object[] values) {
		validate(keys, values);
		StringBuilder sb = new StringBuilder(PREFIX);

		for (int i = 0; i < keys.length; i++) {
			sb.append(keys[i])
				.append(DOMAIN_SEPARATOR)
				.append(values[i]);

			if (i < keys.length - 1) {
				sb.append(PAIR_SEPARATOR);
			}
		}

		return sb.toString();
	}

	private static void validate(String[] keys, Object[] values) {
		if (keys == null || values == null) {
			throw new CustomIllegalArgumentException(ErrorCode.ERR_LOCK_KEY_001);
		}

		if (keys.length != values.length) {
			throw new CustomIllegalArgumentException(ErrorCode.ERR_LOCK_KEY_002);
		}
	}
}
