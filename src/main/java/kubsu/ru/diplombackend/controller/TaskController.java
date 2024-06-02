package kubsu.ru.diplombackend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import kubsu.ru.diplombackend.controller.dto.TaskView;
import kubsu.ru.diplombackend.controller.dto.response.AllTasksResponse;
import kubsu.ru.diplombackend.services.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/task")
public class TaskController {
    private final TaskService taskService;

    @GetMapping("/all")
    public AllTasksResponse getAllTasks() {
        return AllTasksResponse.builder()
                .tasks(taskService.getAllTasks())
                .build();
    }

    @GetMapping("/{taskId}")
    public TaskView getTask(@PathVariable Long taskId) {
        return taskService.getTaskViewById(taskId);
    }

    @PostMapping("/save")
    public AllTasksResponse saveTask(@RequestBody TaskView task) throws JsonProcessingException {
        taskService.save(task);
        return getAllTasks();
    }
}
