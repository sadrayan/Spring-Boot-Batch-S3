package com.nellietech.batch;

import com.nellietech.batch.tasks.ScheduledTasks;
import org.awaitility.Duration;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class ScheduledTasksTest {

	@SpyBean
	ScheduledTasks tasks;

	@Test
	@Disabled("need to be fixed with daily cron")
	public void reportCurrentTime() {
		await().atMost(Duration.ONE_MINUTE).untilAsserted(() -> {
			verify(tasks, atLeast(1)).perform();
		});
	}
}
