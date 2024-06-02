package kubsu.ru.diplombackend.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskSolutionResponse {
    /**
     * Засчитано ли решение студента
     */
    private Boolean isCounted;
    private String taskId;
    private String executionTime;
    private String failedTestInput;
}
