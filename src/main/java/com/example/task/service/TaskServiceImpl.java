package com.example.task.service;

import com.example.task.model.TaskInputData;
import com.example.task.model.TaskResult;
import com.example.task.model.TaskStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class TaskServiceImpl implements TaskService {

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#%");
    private final Map<String, Double> statusMap = new HashMap<>();

    @Async
    @Override
    public void findBestMatchAsync(final TaskInputData taskInputData) {
        final TaskResult bestMatch = findBestMatch(taskInputData);
        System.out.println("bestMatch = " + bestMatch);
    }

    @Override
    public TaskResult findBestMatch(final TaskInputData taskInputData) {
        System.out.println("Invoking an asynchronous method. "
                + Thread.currentThread().getName());
        final String input = taskInputData.input();
        final String pattern = taskInputData.pattern();
        int inputLength = input.length();
        int patternLength = pattern.length();
        int minTypos = Integer.MAX_VALUE;
        int position = 0;

        //validate Input Data

        final int endOfIteration = inputLength - patternLength;
        for (int i = 0; i <= endOfIteration; i++) {
            final long sleepTime = ThreadLocalRandom.current().nextInt(0, 1000);

            final double progress = (double) i / endOfIteration;
            System.out.println("Progress = " + progress + ", SleepTime: " + sleepTime);
            sleep(sleepTime);
            statusMap.put(taskInputData.id(), progress);
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
        System.out.println("Task Finished: " + Thread.currentThread().getName());
        return taskResult(position, typos);
    }

    @Override
    public TaskStatus getTaskStatus(String id) {
        final Double progress = statusMap.get(id);
        return progress != null ?
                new TaskStatus(id, DECIMAL_FORMAT.format(progress))
                : null;
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
