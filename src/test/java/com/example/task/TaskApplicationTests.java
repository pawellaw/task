package com.example.task;

import com.example.task.model.TaskInputData;
import com.example.task.model.TaskStatus;
import com.example.task.service.TaskService;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.Assert;

import java.util.UUID;

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
        String aaa = "AAA";
        for (int i = 0; i <= 15; i++) {
            taskService.findBestMatchAsync(sampleTaskInputData(aaa.repeat(i)));
        }
        Awaitility.await().until(() -> taskExecutor.getThreadPoolExecutor().getCompletedTaskCount() == 15);
    }

    @Test
    void statusCheck() {
        long completedTaskCount = taskExecutor.getThreadPoolExecutor().getCompletedTaskCount();
        String aaa = "AAA";
        String id = "id";

        Assert.isNull(taskService.getTaskStatus(id), "TaskStatus should be null for id = " + id);

        taskService.findBestMatchAsync(sampleTaskInputData(aaa, id));

        Awaitility.await()
                .until(() -> taskExecutor.getThreadPoolExecutor().getCompletedTaskCount() == completedTaskCount + 1);

        TaskStatus taskStatus = taskService.getTaskStatus(id);
        Assert.hasText(taskStatus.getProgress(), "100%");
    }

    private TaskInputData sampleTaskInputData(final String input) {
        return sampleTaskInputData(input, UUID.randomUUID().toString());
    }

    private TaskInputData sampleTaskInputData(final String input, final String id) {
        return TaskInputData.builder()
                .input(input + "B")
                .pattern("B")
                .id(id)
                .build();
    }
}
