package kubsu.ru.diplombackend.repository;

import kubsu.ru.diplombackend.entity.TestCase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestCaseRepository extends JpaRepository<TestCase, Long> {
    List<TestCase> findAllByTaskId(Long taskId);
}
