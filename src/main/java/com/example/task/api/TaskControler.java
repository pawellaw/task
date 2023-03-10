package com.example.task.api;

import com.example.task.model.TaskInfo;
import com.example.task.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.text.DecimalFormat;

@RestController
@RequestMapping("/api/tasks")
@Validated
public class TaskControler {

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#%");

    @Autowired
    private TaskService taskService;

    /**
     * Create task and starts asynchronous processing
     * @param createTaskRequest - Request with pattern and input
     * @return CreateTaskResponse - with cretaed task identifier
     */
    @PostMapping()
    @ResponseStatus(HttpStatus.ACCEPTED)
    @ApiResponses(value = {@ApiResponse(responseCode = "400", description = "Request params incorrect values")})
    @Operation(summary = "Creates new task for asynchronous processing")
    public CreateTaskResponse createTask(@Valid @RequestBody CreateTaskRequest createTaskRequest) {
        final String taskId = taskService.createTask(createTaskRequest.input(), createTaskRequest.pattern());
        taskService.startAsyncProcessing(taskId);

        return new CreateTaskResponse(taskId);
    }

    /**
     * Returns task details based on taskI
     * d
     * @param taskId - Task identifier
     * @return Task - Task details
     */
    @GetMapping("{taskId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Return all information about created task")
    public Task getTask(@PathVariable String taskId) {
        return taskService.getTaskInfo(taskId).map(this::mapToTask).orElse(null);
    }

    /**
     * Rest Api method to return list of all processed tasks
     * @return GetTasksResponse - response with list of processed tasks
     */
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Return list of all tasks processed and their details")
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