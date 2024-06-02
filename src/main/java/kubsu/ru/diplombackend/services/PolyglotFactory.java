package kubsu.ru.diplombackend.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.nimbusds.jose.shaded.gson.JsonParser;
import jakarta.annotation.Nullable;
import kubsu.ru.diplombackend.controller.dto.InputTypeView;
import kubsu.ru.diplombackend.dto.LanguageEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Сервис по обработке скриптовых языков программирования
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PolyglotFactory {
    private static final ObjectMapper mapper = new ObjectMapper();

    private Context getContextByLanguage(LanguageEnum language) {
        switch (language) {
            case javascript -> {
                return Context.create();
            }
            case R, Ruby -> {
                return Context.newBuilder().allowAllAccess(true).build();
            }
            case python -> {
                return Context.newBuilder().allowIO(true).build();
            }
            default -> throw new RuntimeException("Unsupported language: " + language);
        }
    }


    public JsonNode executeScript(String script, @Nullable JsonNode inputData, LanguageEnum languageEnum, List<InputTypeView> inputTypeViews) throws JsonProcessingException {
        Context context = getContextByLanguage(languageEnum);
        StringBuilder builder = new StringBuilder();
        CodeExampleGeneratorUtils.generateInputData(inputTypeViews, languageEnum, builder);
        String inputDataView = builder.toString();
        String replaced = StringUtils.replace(script, inputDataView, "");
        String inputDataByLanguage = createInputDataByLanguage(languageEnum, inputData);
        try {
            String resultScript = inputDataByLanguage + replaced;
            Value result = context.eval(languageEnum.toString(), resultScript);
            Object object = result.as(Object.class);

            try {
                String string = object.toString();
                ObjectNode objectNode = mapper.createObjectNode();
                if (StringUtils.startsWith(string, "{")) {
                    String canonicalFormat = JsonParser.parseString(string).toString();
                    objectNode.set("result", mapper.readTree(canonicalFormat));
                } else {
                    objectNode.put("result", string);
                }
                return objectNode;
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
            return mapper.createObjectNode();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return mapper.createObjectNode();

    }

    private static String createInputDataByLanguage(LanguageEnum languageEnum, JsonNode inputNode) {
        switch (languageEnum) {
            case javascript -> {
                return "const inputView = " +
                        inputNode.toString() +
                        ";" +
                        "\n";
            }
            case python -> {
                return "inputView = '" +
                        inputNode.toString() +
                        "'" +
                        "\n";
            }
            default -> {
                return "";
            }
        }
    }
}
