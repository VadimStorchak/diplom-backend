package kubsu.ru.diplombackend.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import kubsu.ru.diplombackend.controller.dto.request.SolutionTaskRequest;
import kubsu.ru.diplombackend.entity.Solution;
import kubsu.ru.diplombackend.entity.SolutionStatusEnum;
import kubsu.ru.diplombackend.entity.Task;
import kubsu.ru.diplombackend.repository.SolutionRepository;
import kubsu.ru.diplombackend.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SolutionService {
    private final SolutionRepository solutionRepository;
    private final TaskService taskService;
    private final UserService userService;
    private final ApplicationEventPublisher eventPublisher;
    private final CheckSolutionService checkSolutionService;
    private final SolutionExportService solutionExportService;
    private final TaskRepository taskRepository;

    public boolean sendStudentTask(SolutionTaskRequest request) throws JsonProcessingException {
        Task task = taskService.getTaskById(request.getTaskId());
        Solution solution = Solution.fromSolutionTaskRequest(request, task);
        solution.setIsSuccessfully(false);
        solution.setStatus(SolutionStatusEnum.confirm);
        Solution savedSolution = solutionRepository.save(solution);
        return checkSolutionService.executeSolution(savedSolution).getIsSuccessfully();
    }


    public List<Solution> getSolutionsByTaskId(Long taskId) {
        return solutionRepository.findAllByTaskId(taskId);
    }

    public File exportSolution(Long solutionId, String fio) throws IOException {
        Solution solution = solutionRepository.findById(solutionId)
                .orElseThrow(RuntimeException::new);
        Task task = taskService.getTaskById(solution.getTaskId());
        solution.setTask(task);
        return solutionExportService.exportSolutionWord(solution, fio);
    }
}
