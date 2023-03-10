package com.example.task;

import com.example.task.model.TaskStatus;
import com.example.task.service.TaskService;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@SpringBootTest
class TaskApplicationTests {

    @Autowired
    TaskService taskService;

    @Autowired
    ThreadPoolTaskExecutor taskExecutor;

    @Test
    void contextLoads() {

    }

    @Test
    void asyncProcessing() {
        final long completedTaskCount = taskExecutor.getThreadPoolExecutor().getCompletedTaskCount();
        String aaa = "AAA";

        final int taskCount = 10;
        for (int i = 0; i <= taskCount; i++) {
            final String input = aaa.repeat(i) + "B";
            final String pattern = "B";
            final String taskId = taskService.createTask(input, pattern);
            taskService.startAsyncProcessing(taskId);
        }

        Awaitility.await()
                .until(() -> taskService.getTasksList().stream().anyMatch(t -> t.getStatus() == TaskStatus.INPROGRESS));

        Awaitility.await()
                .until(() -> taskService.getTasksList().stream().anyMatch(t -> t.getStatus() == TaskStatus.CREATED));

        Awaitility.waitAtMost(Duration.of(20, ChronoUnit.SECONDS))
                .until(() -> taskExecutor.getThreadPoolExecutor()
                        .getCompletedTaskCount() == completedTaskCount + taskCount);

        Awaitility.await()
                .until(() -> taskService.getTasksList().stream().allMatch(t -> t.getStatus() == TaskStatus.FINISHED));
    }

    @Test
    void statusCheck() {
        long completedTaskCount = taskExecutor.getThreadPoolExecutor().getCompletedTaskCount();

        final String input = "AAAB";
        final String pattern = "B";

        final String id = taskService.createTask(input, pattern);
        taskService.startAsyncProcessing(id);

        Awaitility.await()
                .until(() -> taskService.getTaskInfo(id)
                        .map(t -> t.getStatus() == TaskStatus.INPROGRESS)
                        .orElse(false));

        Awaitility.await()
                .until(() -> taskExecutor.getThreadPoolExecutor().getCompletedTaskCount() == completedTaskCount + 1);

        Awaitility.await()
                .until(() ->
                        taskService.getTaskInfo(id)
                                .map(taskInfo -> taskInfo.getProgress() == 1.0d && taskInfo.getStatus() == TaskStatus.FINISHED)
                                .orElse(false)
                );

        Awaitility.await()
                .until(() -> taskService.getTasksList().size() == completedTaskCount + 1);
    }
}
