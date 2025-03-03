package en_bpdev_Task_Management_System.controller;

import en_bpdev_Task_Management_System.entity.Task;
import en_bpdev_Task_Management_System.service.TaskService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    @GetMapping("/{id}")
    public Optional<Task> getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id);
    }

    @PostMapping
    public ResponseEntity createTask(@RequestBody Task task) {

        taskService.createTask(task);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", String.valueOf(task.getId()));

        return new ResponseEntity(headers, HttpStatus.CREATED);

    }

    @PutMapping("/{id}")
    public ResponseEntity updateTask(@PathVariable Long id, @RequestBody Task taskDetails) {
        taskService.updateTask(id, taskDetails);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteTask(@PathVariable Long id) {

        taskService.deleteTask(id);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
