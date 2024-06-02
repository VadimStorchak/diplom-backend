package kubsu.ru.diplombackend.repository;

import kubsu.ru.diplombackend.entity.Solution;
import kubsu.ru.diplombackend.entity.SolutionStatusEnum;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolutionRepository extends JpaRepository<Solution, Long> {
    List<Solution> findAllByStatus(SolutionStatusEnum status, Limit limit);
    List<Solution> findAllByTaskId(Long taskId);
}
