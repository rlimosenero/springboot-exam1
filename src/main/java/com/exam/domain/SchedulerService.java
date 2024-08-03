package com.exam.domain;
import com.exam.domain.task.Task;
import com.exam.domain.task.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;


@Service
public class SchedulerService {

    @Autowired
    TaskRepository taskRepository;

    public List<Task> calculateSchedule(List<Task> tasks) {

        Map<Task, LocalDate> startDates = new HashMap<>();
        Map<Task, LocalDate> endDates = new HashMap<>();

        // Initialize start dates and end dates
        setInitialDate(tasks,startDates,endDates);
        //validate here
        validateDependencies(tasks);
        detectCycles(tasks);
        // Calculate the schedule
        calculateSchedule(tasks,startDates,endDates);

        // Update the tasks with the calculated dates
        updateCalculatedDates(tasks,startDates,endDates);

        return taskRepository.saveAll(tasks);

    }
    void setInitialDate(List<Task> tasks,Map startDates,Map endDates){
        for (Task task : tasks) {
            startDates.put(task, LocalDate.MIN); // Start from a very old date
            endDates.put(task, LocalDate.MIN);   // Start from a very old date
        }
    }
//    private void validateDependencies(List<Task> tasks) {
//        Set<Long> taskIds = new HashSet<>();
//        for (Task task : tasks) {
//            taskIds.add(task.getId());
//        }
//
//        for (Task task : tasks) {
//            for (Task dependency : task.getDependencies()) {
//                if (!taskIds.contains(dependency.getId())) {
//                    throw new IllegalArgumentException("Task " + task.getName() + " has an invalid dependency: " + dependency.getId());
//                }
//            }
//        }
//    }
    private void detectCycles(List<Task> tasks) {
        Set<Task> visited = new HashSet<>();
        Set<Task> recStack = new HashSet<>();

        for (Task task : tasks) {
            if (detectCyclesUtil(task, visited, recStack)) {
                throw new IllegalStateException("Cycle detected in the task dependencies");
            }
        }
    }

    private boolean detectCyclesUtil(Task task, Set<Task> visited, Set<Task> recStack) {
        if (recStack.contains(task)) {
            return true; // Cycle detected
        }

        if (visited.contains(task)) {
            return false;
        }

        visited.add(task);
        recStack.add(task);

        for (Task dependency : task.getDependencies()) {
            if (detectCyclesUtil(dependency, visited, recStack)) {
                return true;
            }
        }

        recStack.remove(task);
        return false;
    }

    //this is where the taskDates are calculated
    void calculateSchedule(List<Task> tasks,Map startDates,Map endDates){
        for (Task task : tasks) {
            calculateTaskDates(task, startDates, endDates, tasks);
        }
    }

    void updateCalculatedDates(List<Task> tasks, Map<Task,LocalDate> startDates, Map<Task,LocalDate> endDates){
        for (Task task : tasks) {
            LocalDate startDate = startDates.get(task);
            LocalDate endDate = endDates.get(task);
            System.out.println("Task: " + task.getName() + ", Start Date: " + startDates.get(task) + ", End Date: " + endDates.get(task));
            task.setStartDate(startDate);
            task.setEndDate(endDate);
        }
    }


    private void calculateTaskDates(Task task, Map<Task, LocalDate> startDates, Map<Task, LocalDate> endDates, List<Task> allTasks) {
        if (startDates.get(task).isAfter(LocalDate.MIN)) {
            return;
        }

        LocalDate earliestStartDate = LocalDate.now();

        System.out.println("Calculating dates for task: " + task.getName());

        for (Task dependency : task.getDependencies()) {
            if (!startDates.containsKey(dependency)) {
                calculateTaskDates(dependency, startDates, endDates, allTasks);
            }
            LocalDate dependencyEndDate = endDates.get(dependency);
            if (dependencyEndDate != null) {
                LocalDate dependencyStartDate = dependencyEndDate.plusDays(1);
                if (dependencyStartDate.isAfter(earliestStartDate)) {
                    earliestStartDate = dependencyStartDate;
                }
            }
        }

        LocalDate taskStartDate = earliestStartDate;
        LocalDate taskEndDate = taskStartDate.plusDays(task.getDuration() - 1);

        System.out.println("Task: " + task.getName() + ", Start Date: " + taskStartDate + ", End Date: " + taskEndDate);

        startDates.put(task, taskStartDate);
        endDates.put(task, taskEndDate);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task postTask(Task task) {

        return taskRepository.save(task);
    }
    // Method to validate if all dependencies exist
    public void validateDependencies(List<Task> tasks) {
        for (Task task : tasks) {
            for (Task dependency : task.getDependencies()) {
                if (!taskRepository.existsById(dependency.getId())) {
                    throw new IllegalArgumentException("Task " + task.getName() + " has a dependency that is not existing. Please Make sure that Task "+ dependency.getId()+" is created before creating this task " );
                }
            }
        }
    }
}
