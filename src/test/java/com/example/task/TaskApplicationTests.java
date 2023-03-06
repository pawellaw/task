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
        final long completedTaskCount = taskExecutor.getThreadPoolExecutor().getCompletedTaskCount();
        String aaa = "AAA";
        final int taskCount = 15;
        for (int i = 0; i <= taskCount; i++) {
            taskService.findBestMatchAsync(sampleTaskInputData(aaa.repeat(i)));
        }
        Awaitility.await()
                .until(() -> taskExecutor.getThreadPoolExecutor()
                        .getCompletedTaskCount() == completedTaskCount + taskCount);
    }

    @Test
    void statusCheck() {
        long completedTaskCount = taskExecutor.getThreadPoolExecutor().getCompletedTaskCount();
        System.out.println("completedTaskCount = " + completedTaskCount);
        String aaa = "AAA";
        String id = "id";

        Assert.isNull(taskService.getTaskStatus(id), "TaskStatus should be null for id = " + id);

        taskService.findBestMatchAsync(sampleTaskInputData(aaa, id));

        Awaitility.await()
                .until(() -> taskExecutor.getThreadPoolExecutor().getCompletedTaskCount() == completedTaskCount + 1);

        TaskStatus taskStatus = taskService.getTaskStatus(id);
        Assert.hasText(taskStatus.progres(), "100%");
    }

    private TaskInputData sampleTaskInputData(final String input) {
        return sampleTaskInputData(input, UUID.randomUUID().toString());
    }

    private TaskInputData sampleTaskInputData(final String input, final String id) {
        return new TaskInputData(id, input + "B", "B");
    }
}
