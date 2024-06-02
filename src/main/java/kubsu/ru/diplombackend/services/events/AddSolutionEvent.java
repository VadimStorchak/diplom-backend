package kubsu.ru.diplombackend.services.events;

import kubsu.ru.diplombackend.entity.Solution;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class AddSolutionEvent extends ApplicationEvent {
    private final transient Solution solution;


    public AddSolutionEvent(Object source, Solution solution) {
        super(source);
        this.solution = solution;
    }
}
