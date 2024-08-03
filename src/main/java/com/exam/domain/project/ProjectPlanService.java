package com.exam.domain.project;

import com.exam.domain.SchedulerService;
import com.exam.domain.task.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectPlanService {

    @Autowired
    private ProjectPlanRepository projectPlanRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private SchedulerService schedulerService;

    public ProjectPlan createProjectPlan(ProjectPlan projectPlan) {
        projectPlan.getTasks().forEach(task -> taskRepository.save(task));
        projectPlan.setTasks(taskRepository.saveAll(projectPlan.getTasks()));
        return projectPlanRepository.save(projectPlan);
    }
    public List<ProjectPlan> getAllProjectPlans() {
        return projectPlanRepository.findAll();
    }
}

