package com.flab.mealmate.aop.lock;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import javax.sql.DataSource;

import org.springframework.stereotype.Component;

import com.flab.mealmate.global.error.exception.BusinessException;
import com.flab.mealmate.global.error.exception.ErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class MySqlLockExecutor implements LockExecutor {

	private final DataSource writeDataSource;
	private final String MYSQL_LOCK_PREFIX = "LOCK:";

	@Override
	public <T> T execute(String key, int waitTime, int leaseTime, TimeUnit timeUnit, Supplier<T> action) {
		String fullKey = MYSQL_LOCK_PREFIX + key;

		try (Connection conn = writeDataSource.getConnection()) {

			if (!acquireLock(conn, fullKey, waitTime)) {
				log.warn("MySQL 락 획득 실패: {}", fullKey);
				throw new BusinessException(ErrorCode.ERR_DB);
			}

			try {
				return action.get();
			} finally {
				releaseLock(conn, fullKey);
			}

		} catch (SQLException e) {
			log.error("MySQL 연결 실패: {}", e);
			throw new BusinessException(ErrorCode.ERR_DB);
		}
	}


	private boolean acquireLock(Connection conn, String fullKey, int timeoutSeconds) {
		try (PreparedStatement stmt = conn.prepareStatement("SELECT GET_LOCK(?, ?)")) {
			stmt.setString(1, fullKey);
			stmt.setInt(2, timeoutSeconds);
			try (ResultSet rs = stmt.executeQuery()) {
				return rs.next() && rs.getInt(1) == 1;
			}
		} catch (SQLException e) {
			log.error("MySQL 락 획득 중 SQLException 발생: {}", fullKey, e);
			throw new BusinessException(ErrorCode.ERR_DB);
		}
	}

	private void releaseLock(Connection conn, String fullKey) {
		try (PreparedStatement stmt = conn.prepareStatement("SELECT RELEASE_LOCK(?)")) {
			stmt.setString(1, fullKey);
			stmt.execute();
			log.debug("MySQL 락 해제 완료: {}", fullKey);
		} catch (SQLException e) {
			log.error("MySQL 락 해제 중 SQLException 발생: {}", fullKey, e);
			throw new BusinessException(ErrorCode.ERR_DB);
		}
	}

}

