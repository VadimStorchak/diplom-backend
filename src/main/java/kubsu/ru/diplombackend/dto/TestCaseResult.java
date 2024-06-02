package kubsu.ru.diplombackend.dto;

import kubsu.ru.diplombackend.entity.Solution;
import kubsu.ru.diplombackend.entity.TestCase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestCaseResult {
    private Solution solution;
    private TestCase testCase;
    private boolean isPositiveResolve;
    private String message;
}
