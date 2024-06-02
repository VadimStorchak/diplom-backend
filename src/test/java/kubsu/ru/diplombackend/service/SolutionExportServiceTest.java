package kubsu.ru.diplombackend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import kubsu.ru.diplombackend.controller.dto.InputTypeView;
import kubsu.ru.diplombackend.controller.dto.OutputTypeView;
import kubsu.ru.diplombackend.dto.InputType;
import kubsu.ru.diplombackend.dto.InputTypesDto;
import kubsu.ru.diplombackend.dto.LanguageEnum;
import kubsu.ru.diplombackend.dto.OutputTypesDto;
import kubsu.ru.diplombackend.entity.Solution;
import kubsu.ru.diplombackend.entity.Task;
import kubsu.ru.diplombackend.services.CodeExampleGeneratorUtils;
import kubsu.ru.diplombackend.services.SolutionExportService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Slf4j
public class SolutionExportServiceTest {

    private final SolutionExportService solutionExportService = new SolutionExportService();

    @Test
    public void test() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<InputTypeView> inputTypeViews = List.of(InputTypeView.builder()
                .name("arr")
                .type(InputType.Array)
                .build());
        List<OutputTypeView> outputTypeViews = List.of(OutputTypeView.builder()
                .name("result")
                .type(InputType.Number)
                .build());

        Task task = Task.builder()
                .id(1000l)
                .title("Главная задача")
                .description("Описание очень сложной задачи")
                .inputTypes(objectMapper.writeValueAsString(new InputTypesDto(inputTypeViews)))
                .outputTypes(objectMapper.writeValueAsString(new OutputTypesDto(outputTypeViews)))
                .build();

        InputTypeView arrView = InputTypeView.builder()
                .name("arr")
                .type(InputType.Array)
                .build();
        OutputTypeView resultView = OutputTypeView.builder()
                .name("result")
                .type(InputType.Number)
                .build();
        StringBuilder builder = new StringBuilder();
        CodeExampleGeneratorUtils.generateInputData(List.of(arrView), LanguageEnum.javascript, builder);
        CodeExampleGeneratorUtils.generateOutputData(List.of(resultView), LanguageEnum.javascript, builder);

        String code = """
                        %s
                        \s
                        function main() {
                            let arr = inputView.arr;
                            let count = 0;
                            for (const arrKey in arr) {
                                count += Number.parseInt(arrKey)
                            }
                            outputView.result = count;
                            return outputView;
                        }
                       \s
                        main()
                \s""".formatted(
                builder.toString()
        );

        Solution solution = Solution.builder()
                .id(21992l)
                .code(code)
                .taskId(task.getId())
                .build();


        File file = solutionExportService.exportSolutionWord(solution, "Сторчак Вадим Витальевич");

        log.info(file.getName());
        log.info(file.getAbsolutePath());
    }
}
