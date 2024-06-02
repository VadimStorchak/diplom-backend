package kubsu.ru.diplombackend.controller.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kubsu.ru.diplombackend.dto.InputTypesDto;
import kubsu.ru.diplombackend.dto.LanguageEnum;
import kubsu.ru.diplombackend.dto.OutputTypesDto;
import kubsu.ru.diplombackend.entity.Solution;
import kubsu.ru.diplombackend.entity.Task;
import kubsu.ru.diplombackend.entity.TestCase;
import kubsu.ru.diplombackend.entity.Theme;
import kubsu.ru.diplombackend.services.CodeExampleGeneratorUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

import static kubsu.ru.diplombackend.services.CodeExampleGeneratorUtils.generateInputData;
import static kubsu.ru.diplombackend.services.CodeExampleGeneratorUtils.generateOutputData;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskView {
    private Long id;
    private String name;
    private String description;
    private Theme theme;
    private List<InputTypeView> taskInput;
    private List<OutputTypeView> taskOutput;
    private CodeExampleView codeExample;
    private String inputTemplate;
    private String outputTemplate;
    private List<TestCase> testCases;
    private List<Solution> solutions;

    public static TaskView fromTask(Task task) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            List<InputTypeView> inputTypeViews = Arrays.stream(mapper.convertValue(task.getInputTypes(), InputTypeView[].class)).toList();
            List<OutputTypeView> outputTypeViews = Arrays.stream(mapper.convertValue(task.getOutputTypes(), OutputTypeView[].class)).toList();
            return TaskView.builder()
                    .id(task.getId())
                    .name(task.getTitle())
                    .description(task.getDescription())
                    .theme(task.getTheme())
                    .codeExample(CodeExampleGeneratorUtils.generateCodeExamples(inputTypeViews, outputTypeViews))
                    .taskInput(inputTypeViews)
                    .taskOutput(outputTypeViews)
                    .build();
        } catch (Exception e) {
            return TaskView.builder()
                    .id(task.getId())
                    .name(task.getTitle())
                    .description(task.getDescription())
                    .theme(task.getTheme())
                    .build();
        }
    }

    public static TaskView fromTask(Task task, List<Solution> solutions, List<TestCase> testCases) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            List<InputTypeView> inputTypeViews = ((InputTypesDto) mapper.reader().forType(InputTypesDto.class).readValue(task.getInputTypes())).getInputTypeViews();
            List<OutputTypeView> outputTypeViews = ((OutputTypesDto) mapper.reader().forType(OutputTypesDto.class).readValue(task.getOutputTypes())).getOutputTypeViews();
            StringBuilder inputBuilder = new StringBuilder();
            StringBuilder outputBuilder = new StringBuilder();
            generateInputData(inputTypeViews, LanguageEnum.javascript, inputBuilder);
            generateOutputData(outputTypeViews, LanguageEnum.javascript, outputBuilder);
            String inputTemplate = inputBuilder.toString()
                    .replace("const inputView = ", "")
                    .replace(";", "")
                    .replace("/* Array */", "")
                    .replace("/* Float */", "")
                    .replace("/* Number */", "")
                    .replace("/* Boolean */", "");
            String outputTemplate = outputBuilder.toString()
                    .replace("const outputView = ", "")
                    .replace(";", "")
                    .replace("/* Array */", "")
                    .replace("/* Float */", "")
                    .replace("/* Number */", "")
                    .replace("/* Boolean */", "");
            return TaskView.builder()
                    .id(task.getId())
                    .name(task.getTitle())
                    .description(task.getDescription())
                    .theme(task.getTheme())
                    .codeExample(CodeExampleGeneratorUtils.generateCodeExamples(inputTypeViews, outputTypeViews))
                    .inputTemplate(inputTemplate)
                    .outputTemplate(outputTemplate)
                    .taskInput(inputTypeViews)
                    .taskOutput(outputTypeViews)
                    .testCases(testCases)
                    .solutions(solutions)
                    .build();
        } catch (Exception e) {
            return TaskView.builder()
                    .id(task.getId())
                    .name(task.getTitle())
                    .description(task.getDescription())
                    .theme(task.getTheme())
                    .testCases(testCases)
                    .solutions(solutions)
                    .build();
        }
    }

    public Task toTask() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        return Task.builder()
                .id(id)
                .title(name)
                .description(description)
                .theme(theme)
                .inputTypes(mapper.writeValueAsString(new InputTypesDto(taskInput)))
                .outputTypes(mapper.writeValueAsString(new OutputTypesDto(taskOutput)))
                .build();
    }
}
