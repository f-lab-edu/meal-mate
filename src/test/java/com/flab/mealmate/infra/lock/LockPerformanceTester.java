package com.flab.mealmate.infra.lock;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class LockPerformanceTester {
	
	public static void runConcurrentTest(LockExecutor executor, String label) throws InterruptedException {
		int threadCount = 100;
		ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
		CountDownLatch latch = new CountDownLatch(threadCount);
		AtomicInteger successCount = new AtomicInteger();

		long startTime = System.currentTimeMillis();

		for (int i = 0; i < threadCount; i++) {
			executorService.submit(() -> {
				try {
					executor.execute("meetup-1", 5, 3, TimeUnit.SECONDS, () -> {
						sleep();
						successCount.incrementAndGet();
						return null;
					});
				} catch (Exception ignored) {
				} finally {
					latch.countDown();
				}
			});
		}

		latch.await();
		long endTime = System.currentTimeMillis();

		System.out.printf("[%s] Success: %d / %d, Elapsed: %d ms%n", label, successCount.get(), threadCount, endTime - startTime);
		executorService.shutdown();
	}

	private static void sleep() {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}
