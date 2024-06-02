package kubsu.ru.diplombackend.service;

import kubsu.ru.diplombackend.controller.dto.CodeExampleView;
import kubsu.ru.diplombackend.controller.dto.InputTypeView;
import kubsu.ru.diplombackend.controller.dto.OutputTypeView;
import kubsu.ru.diplombackend.dto.InputType;
import kubsu.ru.diplombackend.services.CodeExampleGeneratorUtils;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CodeExampleGeneratorUtilsTest {

    @Test
    public void generateCodeExample() {
        CodeExampleView codeExampleView = CodeExampleGeneratorUtils.generateCodeExamples(List.of(InputTypeView.builder()
                        .name("arr")
                        .type(InputType.Array)
                        .build()),
                List.of(OutputTypeView.builder()
                        .name("result")
                        .type(InputType.Array)
                        .build())
        );

        assertThat(codeExampleView).isNotNull();
    }
}
