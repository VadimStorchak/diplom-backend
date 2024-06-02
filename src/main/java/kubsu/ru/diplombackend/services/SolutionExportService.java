package kubsu.ru.diplombackend.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import kubsu.ru.diplombackend.controller.dto.InputTypeView;
import kubsu.ru.diplombackend.controller.dto.OutputTypeView;
import kubsu.ru.diplombackend.dto.InputTypesDto;
import kubsu.ru.diplombackend.dto.LanguageEnum;
import kubsu.ru.diplombackend.dto.OutputTypesDto;
import kubsu.ru.diplombackend.dto.TestCaseResult;
import kubsu.ru.diplombackend.dto.TestCaseResultView;
import kubsu.ru.diplombackend.entity.Solution;
import kubsu.ru.diplombackend.entity.SolutionStatusEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static kubsu.ru.diplombackend.services.CodeExampleGeneratorUtils.getCodeByInputType;
import static kubsu.ru.diplombackend.services.CodeExampleGeneratorUtils.getCodeByOutputType;

@Slf4j
@Service
@RequiredArgsConstructor
public class SolutionExportService {

    private static String black = "000000";
    private final ObjectMapper jacksonObjectMapper = new ObjectMapper().configure(
            DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT,
            true);

    public File exportSolutionWord(Solution solution, String fio) throws IOException {
        if (solution.getStatus() != SolutionStatusEnum.checked) {
            throw new RuntimeException("Решение не прошло проверку");
        }
        XWPFDocument document = new XWPFDocument();

        XWPFParagraph title = document.createParagraph();
        title.setAlignment(ParagraphAlignment.CENTER);

        XWPFRun titleRun = title.createRun();
        titleRun.setText("Задача #" + solution.getTaskId());
        titleRun.setColor(black);
        titleRun.setBold(true);
        titleRun.setFontFamily("Courier");
        titleRun.setFontSize(20);

        XWPFParagraph taskSubtitle = document.createParagraph();
        taskSubtitle.setAlignment(ParagraphAlignment.LEFT);

        XWPFRun subTitleRun = taskSubtitle.createRun();
        subTitleRun.setText("Текст задачи:");
        setSubtitleStyle(subTitleRun);

        XWPFParagraph taskDescription = document.createParagraph();
        taskDescription.setAlignment(ParagraphAlignment.LEFT);

        XWPFRun descriptionRun = taskDescription.createRun();
        descriptionRun.setText(solution.getTask().getDescription());
        setDescriptionStyle(descriptionRun);
        descriptionRun.setUnderline(UnderlinePatterns.DOT_DOT_DASH);

        XWPFParagraph inputParamsSubtitle = document.createParagraph();
        inputParamsSubtitle.setAlignment(ParagraphAlignment.LEFT);

        XWPFRun inputParamsSubtitleRun = inputParamsSubtitle.createRun();
        inputParamsSubtitleRun.setText("Входные параметры");
        setSubtitleStyle(inputParamsSubtitleRun);


        XWPFParagraph inputParamsDes = document.createParagraph();
        inputParamsDes.setAlignment(ParagraphAlignment.LEFT);

        XWPFRun inputParamsDesRun = inputParamsDes.createRun();
        inputParamsDesRun.setText(toPrettyInput(solution.getTask().getInputTypes()));
        setDescriptionStyle(inputParamsDesRun);

        XWPFParagraph outputParamsSub = document.createParagraph();
        outputParamsSub.setAlignment(ParagraphAlignment.LEFT);

        XWPFRun outputParamsSubRun = outputParamsSub.createRun();
        outputParamsSubRun.setText("Выходные параметры");
        setSubtitleStyle(outputParamsSubRun);


        XWPFParagraph outputParamsDes = document.createParagraph();
        outputParamsDes.setAlignment(ParagraphAlignment.LEFT);

        XWPFRun outputParamsDesRun = outputParamsDes.createRun();
        outputParamsDesRun.setText(toPrettyOutput(solution.getTask().getOutputTypes()));
        setDescriptionStyle(outputParamsDesRun);

        XWPFParagraph countTests = document.createParagraph();
        countTests.setAlignment(ParagraphAlignment.LEFT);

        XWPFRun countTestsRun = countTests.createRun();
        TestCaseResultView testCaseResults = jacksonObjectMapper.reader()
                .forType(TestCaseResultView.class)
                .readValue(Optional.ofNullable(solution.getTestResults()).orElse("{}"));
        if (!CollectionUtils.isEmpty(testCaseResults.getTestCaseResultList())) {
            List<TestCaseResult> positiveSolution = testCaseResults.getTestCaseResultList().stream().filter(TestCaseResult::isPositiveResolve).toList();
            countTestsRun.setText("Пройдено тестов - %s/%s тестов".formatted(positiveSolution.size(), testCaseResults.getTestCaseResultList().size()));
        } else {
            countTestsRun.setText("Пройдено тестов - 0/0 тестов");
        }
        setSubtitleStyle(countTestsRun);

        XWPFParagraph author = document.createParagraph();
        author.setAlignment(ParagraphAlignment.LEFT);

        XWPFRun authorRun = author.createRun();
        authorRun.setText("Работу выполнил %s".formatted(fio));
        setSubtitleStyle(authorRun);

        XWPFParagraph codeSub = document.createParagraph();
        codeSub.setAlignment(ParagraphAlignment.LEFT);

        XWPFRun codeSubRun = codeSub.createRun();
        codeSubRun.setText("Код програмы:");
        setDescriptionStyle(codeSubRun);

        Arrays.stream(solution.getCode().split("\n")).forEachOrdered(codeLine -> {
            XWPFParagraph code = document.createParagraph();
            code.setAlignment(ParagraphAlignment.LEFT);

            XWPFRun codeRun = code.createRun();
            codeRun.setText(codeLine);
            setCodeStyle(codeRun);
        });


        File file = new File("./tmp");
        file.mkdirs();
        File fileExport = new File("./tmp/solution_" + solution.getId() + ".docx");
        fileExport.delete();
        fileExport.createNewFile();
        try (FileOutputStream outputStream = new FileOutputStream(fileExport)) {
            document.write(outputStream);
            document.close();
        } catch (Exception e) {
        }
        return fileExport;

    }

    private static void setSubtitleStyle(XWPFRun outputParamsSubRun) {
        outputParamsSubRun.setColor(black);
        outputParamsSubRun.setFontFamily("Courier");
        outputParamsSubRun.setFontSize(16);
        outputParamsSubRun.setTextPosition(20);
    }

    private static void setDescriptionStyle(XWPFRun outputParamsDesRun) {
        outputParamsDesRun.setColor(black);
        outputParamsDesRun.setFontFamily("Courier");
        outputParamsDesRun.setFontSize(16);
        outputParamsDesRun.setItalic(true);
        outputParamsDesRun.setTextPosition(20);
    }

    private static void setCodeStyle(XWPFRun outputParamsDesRun) {
        outputParamsDesRun.setColor(black);
        outputParamsDesRun.setFontFamily("Consolas");
        outputParamsDesRun.setFontSize(12);
        outputParamsDesRun.setTextPosition(20);
    }

    private String toPrettyInput(String typeView) throws JsonProcessingException {
        InputTypesDto typeViews = jacksonObjectMapper.reader().forType(InputTypesDto.class).readValue(typeView);
        List<InputTypeView> inputTypeViews = typeViews.getInputTypeViews();
        StringBuilder builder = new StringBuilder("{");
        for (InputTypeView inputTypeView : inputTypeViews) {
            builder.append(getCodeByInputType(inputTypeView, LanguageEnum.javascript)).append(",");
        }
        builder.deleteCharAt(builder.length() - 1);
        builder.append("};")
                .append("\n");
        return builder.toString();
    }

    private String toPrettyOutput(String typeView) throws JsonProcessingException {
        OutputTypesDto typeViews = jacksonObjectMapper.reader().forType(OutputTypesDto.class).readValue(typeView);
        List<OutputTypeView> outputTypeViews = typeViews.getOutputTypeViews();
        StringBuilder builder = new StringBuilder("{");
        for (OutputTypeView outputTypeView : outputTypeViews) {
            builder.append(getCodeByOutputType(outputTypeView, LanguageEnum.javascript)).append(",");
        }
        builder.deleteCharAt(builder.length() - 1);
        builder.append("};")
                .append("\n");
        return builder.toString();
    }
}
