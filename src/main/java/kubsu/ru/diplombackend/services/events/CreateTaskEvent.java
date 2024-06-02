package kubsu.ru.diplombackend.services.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class CreateTaskEvent extends ApplicationEvent {


    public CreateTaskEvent(Object source) {
        super(source);
    }
}
