package kubsu.ru.diplombackend.services;


import kubsu.ru.diplombackend.entity.Task;
import kubsu.ru.diplombackend.entity.TestCase;
import kubsu.ru.diplombackend.repository.TaskRepository;
import kubsu.ru.diplombackend.repository.TestCaseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TestCaseService {
    private final TestCaseRepository testCaseRepository;
    private final TaskRepository taskRepository;

    public TestCase addTestCase(Long taskId, TestCase testCase) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task with id " + taskId + " not found"));
        testCase.setTask(task);
        return testCaseRepository.save(testCase);
    }

    public List<TestCase> getAllTestsByTaskId(Long taskId) {
        return testCaseRepository.findAllByTaskId(taskId);
    }

    public TestCase save(TestCase testCase) {
        Task task = taskRepository.findById(testCase.getTaskId())
                .orElseThrow(() -> new RuntimeException("Task with id " + testCase.getTaskId() + " not found"));
        testCase.setTask(task);
        return testCaseRepository.save(testCase);
    }

    public List<TestCase> saveAll(List<TestCase> body) {
        List<TestCase> testCases = body.stream().peek(testCase -> {
            Task task = taskRepository.findById(testCase.getTaskId())
                    .orElseThrow(() -> new RuntimeException("Task with id " + testCase.getTaskId() + " not found"));
            testCase.setTask(task);
        }).toList();
        return testCaseRepository.saveAllAndFlush(testCases);
    }
}
