package kubsu.ru.diplombackend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import kubsu.ru.diplombackend.controller.dto.InputTypeView;
import kubsu.ru.diplombackend.controller.dto.OutputTypeView;
import kubsu.ru.diplombackend.dto.InputType;
import kubsu.ru.diplombackend.dto.LanguageEnum;
import kubsu.ru.diplombackend.services.CodeExampleGeneratorUtils;
import kubsu.ru.diplombackend.services.PolyglotFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class PolyglotFactoryTest {
    private PolyglotFactory polyglotFactory = new PolyglotFactory();

    @Test
    void executeScript_testJS_Number() throws JsonProcessingException {
        String code = """
                 let count = 0
                                \s
                 function main() {
                     for (let i = 0; i < Math.floor(Math.random() * 100); i++) {
                         count++;
                     }
                     return count;
                 }
                 
                 main();
                \s""";

        LanguageEnum language = LanguageEnum.javascript;

        JsonNode result = polyglotFactory.executeScript(code, null, language, List.of());

        assertThat(result).isNotNull();
        assertThat(result.get("result").asInt()).isNotNull();
    }

    @Test
    void executeScript_testJS_Double() throws JsonProcessingException {
        String code = """
                 let count = 0.0
                                \s
                 function main() {
                     for (let i = 0; i < Math.floor(Math.random() * 100); i++) {
                         count += Math.random();
                     }
                     return count;
                 }
                 
                 main();
                \s""";

        LanguageEnum language = LanguageEnum.javascript;

        JsonNode result = polyglotFactory.executeScript(code, null, language, List.of());

        assertThat(result).isNotNull();
        assertThat(result.get("result").asDouble()).isNotNull();
    }

    @Test
    void executeScript_testJS_Date() throws JsonProcessingException {
        String code = """
                                \s
                function main() {
                    return new Date();
                }
                              \s
                main()
                \s""";

        LanguageEnum language = LanguageEnum.javascript;

        JsonNode result = polyglotFactory.executeScript(code, null, language, List.of());

        assertThat(result).isNotNull();
        assertThat(result.get("result").isTextual()).isTrue();
    }

    @Test
    void executeScript_testJS_String() throws JsonProcessingException {
        String code = """
                \s
                 function main() {
                     return "hello world"
                 }
                \s
                 main();
                \s""";

        LanguageEnum language = LanguageEnum.javascript;

        JsonNode result = polyglotFactory.executeScript(code, null, language, List.of());

        assertThat(result).isNotNull();
        assertThat(result.get("result").isTextual()).isTrue();
    }

    @Test
    void executeScript_testJS_TestInput() throws JsonProcessingException {
        String code = """
                function main() {
                    let count = 0;
                    inputData.input.forEach((item) => {
                        count += Number.parseInt(item);
                    })
                    return count;
                }
                               \s
                main()
               \s
               \s""";

        LanguageEnum language = LanguageEnum.javascript;
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode arrayNode = objectMapper.createArrayNode();
        List.of(1,2,3,4,5).forEach(arrayNode::add);
        JsonNode result = polyglotFactory.executeScript(code, arrayNode, language, List.of());

        assertThat(result).isNotNull();
        assertThat(result.get("result").asInt()).isEqualTo(15);
    }

    @Test
    void executeScript_testJS_JSON() throws JsonProcessingException {
        String code = """
                 let input = {
                            'id': 1,
                            'arr': [1, 2, 3, 4],
                            'result': 0
                        }
                       \s
                        function main() {
                            let arr = input.arr;
                            let count = 0;
                            for (const arrKey in arr) {
                                count += Number.parseInt(arrKey)
                            }
                            input.result = count;
                            return input;
                        }
                       \s
                        main()
                \s""";

        LanguageEnum language = LanguageEnum.javascript;

        JsonNode result = polyglotFactory.executeScript(code, null, language, List.of());

        assertThat(result).isNotNull();
        assertThat(result.get("result")).isNotNull();
        assertThat(result.get("result").get("result").asInt()).isEqualTo(6);
    }

    @Test
    void executeScript_testJS_JsonNode() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
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

        LanguageEnum language = LanguageEnum.javascript;

        ObjectNode inputData = objectMapper.createObjectNode();
        ArrayNode arrayNode = objectMapper.createArrayNode();
        List.of(1,2,3,4).forEach(arrayNode::add);
        inputData.set("arr", arrayNode);

        JsonNode result = polyglotFactory.executeScript(code, inputData, language, List.of(arrView));

        assertThat(result).isNotNull();
        assertThat(result.get("result")).isNotNull();
        assertThat(result.get("result").get("result").asInt()).isEqualTo(6);
    }
}
