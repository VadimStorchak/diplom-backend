package kubsu.ru.diplombackend.controller.dto.request;

import kubsu.ru.diplombackend.dto.LanguageEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SolutionTaskRequest {
    private Long taskId;
    private String solution;
    private String studentFio;
    private LanguageEnum language;
}
