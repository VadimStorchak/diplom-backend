package kubsu.ru.diplombackend.exception;

import jakarta.persistence.EntityNotFoundException;
import kubsu.ru.diplombackend.controller.dto.response.MessageResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Перехватчик ошибок работы
 */
@Slf4j
@RestControllerAdvice
public class ExceptionHandlingController {

    /**
     * Метод перехватывает {@link EntityNotFoundException}
     * и возвращает 403 ошибку с информацией об исключительной ситуации.
     * <ul>Перехватываемые Exception:
     *     <li>{@link EntityNotFoundException}</li>
     * </ul>
     *
     * @param exception пойманное исключение на уровне контроллеров.
     * @return 403 (FORBIDDEN) ошибку с информацией об исключении.
     */
    @ExceptionHandler(value = EntityNotFoundException.class)
    public ResponseEntity<MessageResponse> handleTokenRefreshException(EntityNotFoundException exception) {
        log.error(exception.getMessage(), exception);
        return new ResponseEntity<>(new MessageResponse(exception.getMessage()), HttpStatus.NOT_FOUND);
    }
}
