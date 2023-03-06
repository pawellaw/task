package com.example.task.api;

import com.example.task.model.TaskInputData;
import com.example.task.model.TaskStatus;
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

import java.util.UUID;

@RestController
@RequestMapping("/api/tasks")
public class TaskControler {

    @Autowired
    private TaskService taskService;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public void getTasks() {
        System.out.println("taskService = " + taskService);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.ACCEPTED)
    public CreateTaskResponse createTask(@RequestBody CreateTaskRequest createTaskRequest) {
        final String uuid = UUID.randomUUID().toString();
        taskService.findBestMatchAsync(
                new TaskInputData(uuid, createTaskRequest.input(), createTaskRequest.pattern()));
        return new CreateTaskResponse(uuid);
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskStatusResponse createTask(@PathVariable String id) {
        final TaskStatus taskStatus = taskService.getTaskStatus(id);
        return new TaskStatusResponse(id, taskStatus.progres());
    }


}