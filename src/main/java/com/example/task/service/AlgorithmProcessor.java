package com.example.task.service;

import com.example.task.model.TaskInputData;
import com.example.task.model.TaskResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@Component
public class AlgorithmProcessor {

    @Autowired
    public AlgorithmProcessor(final TaskStorage taskStorage) {
        this.taskStorage = taskStorage;
    }

    private final Logger logger = LoggerFactory.getLogger(AlgorithmProcessor.class);

    private final TaskStorage taskStorage;

    public TaskResult findBestMatch(final TaskInputData taskInputData) {
        logger.info("Invoking an asynchronous method in thread: " + Thread.currentThread()
                .getName() + "for input Data: " + taskInputData);
        final String input = taskInputData.input();
        final String pattern = taskInputData.pattern();
        final String taskId = taskInputData.id();

        int inputLength = input.length();
        int patternLength = pattern.length();
        int minTypos = Integer.MAX_VALUE;
        int position = 0;

        //validate Input Data

        final int endOfIteration = inputLength - patternLength;
        for (int i = 0; i <= endOfIteration; i++) {
            final long sleepTime = ThreadLocalRandom.current().nextInt(0, 1000);

            final double progress = (double) i / endOfIteration;

            logger.info("TaskId = " + taskId + "Progress = " + progress + ", SleepTime: " + sleepTime);
            sleep(sleepTime);
            taskStorage.get(taskId).updateProgress(progress);

            int typos = checkTypos(input.substring(i, patternLength + i), pattern);
            if (typos == 0) {
                return finishProcessing(i, typos);
            }
            if (typos < minTypos) {
                minTypos = typos;
                position = i;
            }
        }
        return finishProcessing(position, minTypos);
    }

    private static void sleep(final long sleepTime) {
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private TaskResult finishProcessing(final int position, final int typos) {
        logger.info("Task Finished: " + Thread.currentThread().getName());
        return taskResult(position, typos);
    }

    private TaskResult taskResult(final int position, final int typos) {
        return new TaskResult(position, typos);
    }

    private int checkTypos(final String substring, final String pattern) {
        int typos = 0;
        for (int i = 0; i < pattern.length(); i++) {
            if (substring.charAt(i) != pattern.charAt(i)) {
                typos++;
            }
        }
        return typos;
    }
}
