We need to calculate calendar schedules for project plans
 

Each project plan consists of tasks. Every task has a certain duration.
 

A task can depend on zero or more other tasks. If a task depends on some other tasks, it can only be started after these tasks are completed
 

So, for a set of tasks (with durations and dependencies), the solution for the challenge should generate a schedule, i.e. assign Start and End Dates for every task
 
Attached postman collection in git as resource file in the resources folder

How to Test

Either post 3 Task or post project plans

Steps to post 3 Task
1.Post Task
2.Post Task 2
3.Post Task 3
4. Post schedule
5. Verify Dates in the JSON response or in the browser http://localhost:8088/v1/api/tasks

Steps to post project plans
1. Post project plans
2. Post schedule
3. Verify Dates in the JSON response or in the browser localhost:8088/v1/api/projects