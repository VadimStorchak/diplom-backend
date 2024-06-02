package kubsu.ru.diplombackend.controller.dto.request;

import kubsu.ru.diplombackend.dto.TaskDifficults;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FilterTaskRequest {
    private String byName;
    private List<TaskDifficults> byDifficult;
    private List<String> byTheme;
}
