package com.example.task.api;

import com.example.task.model.TaskInfo;
import com.example.task.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.text.DecimalFormat;
import java.util.UUID;

@RestController
@RequestMapping("/api/tasks")
public class TaskControler {

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#%");

    @Autowired
    private TaskService taskService;

    @PostMapping()
    @ResponseStatus(HttpStatus.ACCEPTED)
    public CreateTaskResponse createTask(@RequestBody CreateTaskRequest createTaskRequest) {
        final String taskId = UUID.randomUUID().toString();
        taskService.createTask(taskId, createTaskRequest.input(), createTaskRequest.pattern());
        return new CreateTaskResponse(taskId);
    }

    @GetMapping("{taskId}")
    @ResponseStatus(HttpStatus.OK)
    public Task getTask(@PathVariable String taskId) {
        final TaskInfo taskInfo = taskService.getTaskInfo(taskId);
        return taskInfo != null ? mapToTask(taskInfo) : null;
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public GetTasksResponse getTasks() {
        return new GetTasksResponse(taskService.getTasksList().stream().map(this::mapToTask).toList());
    }

    private Task mapToTask(final TaskInfo taskInfo) {
        return new Task(taskInfo.getId(), taskInfo.getInput(), taskInfo.getPattern(),
                taskInfo.getCreationTime(), taskInfo.getStartProgressTime(), taskInfo.getFinishTime(),
                taskInfo.getStatus().toString(), DECIMAL_FORMAT.format(taskInfo.getProgress()),
                String.valueOf(taskInfo.getPosition()), String.valueOf(taskInfo.getTypos()));
    }
}