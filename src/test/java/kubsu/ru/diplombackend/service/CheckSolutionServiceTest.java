package kubsu.ru.diplombackend.service;

import kubsu.ru.diplombackend.entity.Solution;
import kubsu.ru.diplombackend.entity.SolutionStatusEnum;
import kubsu.ru.diplombackend.repository.SolutionRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@AllArgsConstructor
public class CheckSolutionServiceTest {

    private final SolutionRepository solutionRepository;

    @Test
    public void testLimit() {
        for (int i = 0; i < 20; i++) {
            solutionRepository.save(Solution.builder()
                    .status(SolutionStatusEnum.confirm)
                    .build());
        }

        List<Solution> all = solutionRepository.findAll();
        assertThat(all).isEqualTo(20);
    }
}
