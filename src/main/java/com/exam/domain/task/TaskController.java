package com.exam.domain.task;
import com.exam.domain.SchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/v1/api/tasks")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private SchedulerService schedulerService;

    @GetMapping
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @PostMapping
    public Task createTask(@RequestBody Task task) {
        schedulerService.validateDependencies(Collections.singletonList(task));
        return schedulerService.postTask(task);
    }

    @PostMapping("/schedule")
    public List<Task> scheduleTasks() {
        List<Task> tasks = schedulerService.getAllTasks();
        return schedulerService.calculateSchedule(tasks);

    }


}
