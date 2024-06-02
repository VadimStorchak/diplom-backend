package kubsu.ru.diplombackend.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kubsu.ru.diplombackend.controller.dto.InputTypeView;
import kubsu.ru.diplombackend.dto.InputTypesDto;
import kubsu.ru.diplombackend.dto.LanguageEnum;
import kubsu.ru.diplombackend.dto.TestCaseResult;
import kubsu.ru.diplombackend.dto.TestCaseResultView;
import kubsu.ru.diplombackend.entity.Solution;
import kubsu.ru.diplombackend.entity.SolutionStatusEnum;
import kubsu.ru.diplombackend.entity.Task;
import kubsu.ru.diplombackend.entity.TestCase;
import kubsu.ru.diplombackend.repository.SolutionRepository;
import kubsu.ru.diplombackend.services.events.AddSolutionEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.janino.SimpleCompiler;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.StringReader;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class CheckSolutionService {
    private final Integer queueMaxSize = 2;
    private final Set<Solution> solutionQueue = new LinkedHashSet<>(queueMaxSize);
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final ApplicationEventPublisher eventPublisher;
    private final SolutionRepository solutionRepository;
    private final TestCaseService testCaseService;
    private final PolyglotFactory polyglotFactory;
    private final TaskService taskService;


    public Solution executeSolution(Solution solution) throws JsonProcessingException {
        Task task = taskService.getTaskById(solution.getTaskId());
        List<TestCase> testCases = testCaseService.getAllTestsByTaskId(solution.getTaskId());
        List<TestCaseResult> testResults = testCases.stream().map(testCase -> {
                    switch (solution.getLanguage()) {
                        case java -> {
                            // TODO: Заменить на реальные входные данные
                            Object inputData = new Object();
                            try {
                                SimpleCompiler compiler = new SimpleCompiler();
                                compiler.cook(new StringReader(solution.getCode()));

                                String className = "JavaCompiler";
                                Class<?> clazz = compiler.getClassLoader().loadClass(className);
                                Object instance = clazz.getDeclaredConstructor().newInstance();

                                Method[] methods = clazz.getDeclaredMethods();
                                Object result = null;
                                for (Method method : methods) {
                                    if (method.getName().equals("main")) {
                                        result = method.invoke(instance, inputData);
                                    }
                                }
                                log.info("Java Compiler Result - {}", result);
                            } catch (Exception e) {
                                log.error(e.getMessage(), e);
                            }
                        }
                        case javascript, python -> {

                            try {
                                JsonNode inputData = objectMapper.readTree(testCase.getJsonInput());
                                InputTypesDto typeViews = objectMapper.reader().forType(InputTypesDto.class).readValue(task.getInputTypes());
                                List<InputTypeView> inputTypeViews = typeViews.getInputTypeViews();

                                JsonNode resultNode = polyglotFactory.executeScript(solution.getCode(), inputData, solution.getLanguage(), inputTypeViews);

                                JsonNode result = resultNode.get("result");
                                String resultString = result.toString();
                                String testCaseOutput = objectMapper.readTree(testCase.getJsonOutput()).toString();
                                boolean isEquals = StringUtils.equalsIgnoreCase(testCaseOutput, resultString);
                                return TestCaseResult.builder()
                                        .testCase(testCase)
                                        .isPositiveResolve(isEquals)
                                        .message("Не прошла проверка входных данных - " + testCase.getJsonInput())
                                        .build();
                            } catch (JsonProcessingException e) {
                                return TestCaseResult.builder()
                                        .testCase(testCase)
                                        .isPositiveResolve(false)
                                        .message("Проблемы в Тестовом варианте, обратитесь к преподователю")
                                        .build();
                            }
                        }
                    }
                    return new TestCaseResult();
                })
                .toList();


        long countOfPositiveTests = testResults.stream().filter(TestCaseResult::isPositiveResolve).count();

        solution.setIsSuccessfully(countOfPositiveTests == testResults.size());
        solution.setTestResults(objectMapper.writeValueAsString(new TestCaseResultView(testResults)));
        solution.setStatus(SolutionStatusEnum.checked);
        return solutionRepository.save(solution);
    }
}
