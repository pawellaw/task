package com.example.task.service;

import com.example.task.model.TaskInfo;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TaskStorage {

    private final Map<String, TaskInfo> tasks = new ConcurrentHashMap<>();

    public void put(String id, TaskInfo taskInfo) {
        tasks.put(id, taskInfo);
    }

    public List<TaskInfo> getList() {
        return tasks.values().stream().toList();
    }

    public TaskInfo get(final String taskId) {
        return tasks.get(taskId);
    }
}
