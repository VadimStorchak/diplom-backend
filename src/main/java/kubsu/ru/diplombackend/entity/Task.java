package kubsu.ru.diplombackend.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
@Table(name = "task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "theme_id", referencedColumnName = "id")
    private Theme theme;

    @Column(name = "difficult")
    private String difficult;

    @Column(name = "reference_solution")
    private String referenceSolution;

    @Column(name = "lang")
    private LanguageEnum lang;

    @Column(name = "input_types")
    // В формате JSON
    private String inputTypes;

    @Column(name = "output_types")
    // В формате JSON
    private String outputTypes;
}
