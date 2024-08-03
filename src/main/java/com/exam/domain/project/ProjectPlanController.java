package com.exam.domain.project;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api/projects")
public class ProjectPlanController {

    private final ProjectPlanService projectPlanService;

    public ProjectPlanController(ProjectPlanService projectPlanService) {
        this.projectPlanService = projectPlanService;
    }

    @GetMapping
    public List<ProjectPlan> getAllProjectPlans() {
        return projectPlanService.getAllProjectPlans();
    }

    @PostMapping
    public ProjectPlan createProjectPlan(@RequestBody ProjectPlan projectPlan) {
        return projectPlanService.createProjectPlan(projectPlan);
    }
}

