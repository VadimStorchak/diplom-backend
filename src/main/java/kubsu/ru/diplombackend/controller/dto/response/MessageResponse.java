package kubsu.ru.diplombackend.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Простой ответ сервера, содержащий только сообщение о ситуации
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponse {
    private String message;
}
