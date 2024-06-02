package kubsu.ru.diplombackend.controller.dto.response;

import kubsu.ru.diplombackend.controller.dto.TaskView;
import kubsu.ru.diplombackend.entity.Task;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AllTasksResponse {
    private List<TaskView> tasks;
}
