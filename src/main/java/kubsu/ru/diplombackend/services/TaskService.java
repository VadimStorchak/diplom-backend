package kubsu.ru.diplombackend.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import kubsu.ru.diplombackend.controller.dto.InputTypeView;
import kubsu.ru.diplombackend.controller.dto.OutputTypeView;
import kubsu.ru.diplombackend.controller.dto.TaskView;
import kubsu.ru.diplombackend.controller.dto.response.AllTasksResponse;
import kubsu.ru.diplombackend.dto.InputType;
import kubsu.ru.diplombackend.dto.InputTypesDto;
import kubsu.ru.diplombackend.dto.OutputTypesDto;
import kubsu.ru.diplombackend.entity.Solution;
import kubsu.ru.diplombackend.entity.Task;
import kubsu.ru.diplombackend.entity.TestCase;
import kubsu.ru.diplombackend.entity.Theme;
import kubsu.ru.diplombackend.repository.SolutionRepository;
import kubsu.ru.diplombackend.repository.TaskRepository;
import kubsu.ru.diplombackend.services.events.CreateTaskEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final SolutionRepository solutionRepository;
    private final TestCaseService testCaseService;
    private final ApplicationEventPublisher eventPublisher;

    public List<TaskView> getAllTasks() {

        List<Task> all = taskRepository.findAll();
        return all.stream().map(task -> {
            List<TestCase> testCases = testCaseService.getAllTestsByTaskId(task.getId());
            List<Solution> allByTaskId = solutionRepository.findAllByTaskId(task.getId());
            return TaskView.fromTask(task, allByTaskId, testCases);
        }).toList();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void init() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<InputTypeView> inputTypeViews = List.of(InputTypeView.builder()
                .name("arr")
                .type(InputType.Array)
                .build());
        List<OutputTypeView> outputTypeViews = List.of(OutputTypeView.builder()
                .name("result")
                .type(InputType.Number)
                .build());
        for (int i = 0; i < 20; i++) {
            taskRepository.save(Task.builder()
                    .title("Задача " + UUID.randomUUID())
                    .description("Описание " + UUID.randomUUID())
                    .theme(Theme.builder()
                            .name("Тема " + UUID.randomUUID())
                            .build())
                    .inputTypes(objectMapper.writeValueAsString(new InputTypesDto(inputTypeViews)))
                    .outputTypes(objectMapper.writeValueAsString(new OutputTypesDto(outputTypeViews)))
                    .build());
        }
        eventPublisher.publishEvent(new CreateTaskEvent(this));
    }
    public Task getTaskById(long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException());
    }

    public TaskView getTaskViewById(long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));
        List<TestCase> testCases = testCaseService.getAllTestsByTaskId(task.getId());
        List<Solution> allByTaskId = solutionRepository.findAllByTaskId(task.getId());
        return TaskView.fromTask(task, allByTaskId, testCases);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Task save(TaskView task) throws JsonProcessingException {
        return taskRepository.saveAndFlush(task.toTask());
    }
}
