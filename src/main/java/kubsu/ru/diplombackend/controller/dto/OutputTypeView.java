package kubsu.ru.diplombackend.controller.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import kubsu.ru.diplombackend.dto.InputType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class OutputTypeView {
    private String name;
    private InputType type;

    public InputTypeView toInputTypeView() {
        InputTypeView inputTypeView = new InputTypeView();
        inputTypeView.setName(name);
        inputTypeView.setType(type);
        return inputTypeView;
    }
}
