package kubsu.ru.diplombackend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import kubsu.ru.diplombackend.dto.LanguageEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "task_try_solve")
public class TaskTrySolve {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "task_id", referencedColumnName = "id")
    private Task task;

    @Column(name = "is_solved")
    private Boolean isSolved;

    @OneToOne
    @JoinColumn(name = "solution_id", referencedColumnName = "id")
    private Solution solution;

    // Представлены в формате Json
    @Column(name = "test_result")
    private String testResult;
}
