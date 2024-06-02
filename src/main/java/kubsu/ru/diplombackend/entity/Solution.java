package kubsu.ru.diplombackend.entity;

import jakarta.persistence.*;
import kubsu.ru.diplombackend.controller.dto.request.SolutionTaskRequest;
import kubsu.ru.diplombackend.dto.LanguageEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "solution")
public class Solution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "studentId")
    private String studentId;

    @Column(name = "user_fio")
    private String userFio;

    @Column(name = "task_id")
    private Long taskId;

    @Column(name = "code", length = 10_000)
    private String code;

    @Column(name = "is_successfully")
    private Boolean isSuccessfully;

    @Column(name = "status")
    private SolutionStatusEnum status;

    @Column(name = "language")
    private LanguageEnum language;

    @Column(name="testResult", length = 10_000)
    // JSON
    private String testResults;

    @JsonIgnore
    @Transient
    private Task task;

    public static Solution fromSolutionTaskRequest(SolutionTaskRequest solutionTaskRequest, Task task) {
        return Solution.builder()
                .taskId(task.getId())
                .code(solutionTaskRequest.getSolution())
                .userFio(solutionTaskRequest.getStudentFio())
                .language(solutionTaskRequest.getLanguage())
                .build();
    }
}
