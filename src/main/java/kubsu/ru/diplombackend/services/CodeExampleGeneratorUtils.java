package kubsu.ru.diplombackend.services;

import kubsu.ru.diplombackend.controller.dto.CodeExampleView;
import kubsu.ru.diplombackend.controller.dto.InputTypeView;
import kubsu.ru.diplombackend.controller.dto.OutputTypeView;
import kubsu.ru.diplombackend.dto.LanguageEnum;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class CodeExampleGeneratorUtils {

    public static CodeExampleView generateCodeExamples(List<InputTypeView> inputTypeViews, List<OutputTypeView> outputTypeViews) {
        CodeExampleView codeExampleView = new CodeExampleView();
        codeExampleView.setJavascript(getCodeByLanguage(LanguageEnum.javascript, inputTypeViews, outputTypeViews));
        codeExampleView.setPython(getCodeByLanguage(LanguageEnum.python, inputTypeViews, outputTypeViews));
        codeExampleView.setJava(getCodeByLanguage(LanguageEnum.java, inputTypeViews, outputTypeViews));
        return codeExampleView;
    }

    private static String getCodeByLanguage(LanguageEnum language, List<InputTypeView> inputTypeViews, List<OutputTypeView> outputTypeViews) {
        StringBuilder builder = new StringBuilder();
        generateInputData(inputTypeViews, language, builder);
        generateOutputData(outputTypeViews, language, builder);

        switch (language) {
            case javascript -> {

                builder.append("""
                         function main() {
                             // type your code here
                                                \s
                             return outputView;
                         }
                                                \s
                         main()
                        \s""");
                return builder.toString();
            }
            case python -> {
                String params = builder.toString();
                return """
                        import json
                        %s
                        
                        def main():  
                            output = json.loads(outputView)
                            input = json.loads(inputView)
                            # type your code here
                            
                            return output
                                                
                        main()                      
                        """.formatted(params);
            }

        }
        return "";
    }

    public static void generateOutputData(List<OutputTypeView> outputTypeViews, LanguageEnum language, StringBuilder builder) {
        switch (language) {
            case javascript -> {
                builder.append("const outputView = {");
                for (OutputTypeView outputTypeView : outputTypeViews) {
                    builder.append(getCodeByOutputType(outputTypeView, language)).append(",");
                }
                builder.deleteCharAt(builder.length() - 1);
                builder.append("};")
                        .append("\n");
            }
            case python -> {
                builder.append("outputView = '{");
                for (OutputTypeView outputTypeView : outputTypeViews) {
                    builder.append(getCodeByOutputType(outputTypeView, language)).append(",");
                }
                builder.deleteCharAt(builder.length() - 1);
                builder.append("}'")
                        .append("\n");
            }
        }
    }

    public static void generateInputData(List<InputTypeView> inputTypeViews, LanguageEnum language, StringBuilder builder) {
        switch (language) {
            case javascript -> {
                builder.append("const inputView = {");
                for (InputTypeView inputTypeView : inputTypeViews) {
                    builder.append(getCodeByInputType(inputTypeView, language)).append(",");
                }
                builder.deleteCharAt(builder.length() - 1);
                builder.append("};")
                        .append("\n");
            }
            case python -> {
                builder.append("inputView = '{");
                for (InputTypeView inputTypeView : inputTypeViews) {
                    builder.append(getCodeByInputType(inputTypeView, language)).append(",");
                }
                builder.deleteCharAt(builder.length() - 1);
                builder.append("}'")
                        .append("\n");
            }
        }
    }

    public static String getCodeByInputType(InputTypeView inputTypeView, LanguageEnum language) {
        switch (language) {
            case javascript -> {
                switch (inputTypeView.getType()) {
                    case Array -> {
                        return wrapDoubleQuotes(inputTypeView.getName()) + ":" + "[] /* Array */";
                    }
                    case Float -> {
                        return wrapDoubleQuotes(inputTypeView.getName()) + ":" + "0.0 /* Float */";
                    }
                    case Number -> {
                        return wrapDoubleQuotes(inputTypeView.getName()) + ":" + "0 /* Number */";
                    }
                    case Boolean -> {
                        return wrapDoubleQuotes(inputTypeView.getName()) + ":" + "false /* Boolean */";
                    }
                    default -> {
                        return wrapDoubleQuotes(inputTypeView.getName()) + ":" + wrapDoubleQuotes(inputTypeView.getType().toString()) + "/* String */";
                    }
                }
            }
            case python -> {
                switch (inputTypeView.getType()) {
                    case Array -> {
                        return wrapDoubleQuotes(inputTypeView.getName()) + ":" + "[]";
                    }
                    case Float -> {
                        return wrapDoubleQuotes(inputTypeView.getName()) + ":" + "0.0";
                    }
                    case Number -> {
                        return wrapDoubleQuotes(inputTypeView.getName()) + ":" + "0";
                    }
                    case Boolean -> {
                        return wrapDoubleQuotes(inputTypeView.getName()) + ":" + "false";
                    }
                    default -> {
                        return wrapDoubleQuotes(inputTypeView.getName()) + ":" + wrapDoubleQuotes(inputTypeView.getType().toString()) + "/* String */";
                    }
                }
            }
        }
        return "";
    }

    public static String getCodeByOutputType(OutputTypeView typeView, LanguageEnum language) {
        return getCodeByInputType(typeView.toInputTypeView(), language);
    }

    public static String wrapDoubleQuotes(String s) {
        return "\"" + s + "\"";
    }

}
