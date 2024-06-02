package kubsu.ru.diplombackend.entity;

public enum SolutionStatusEnum {
    confirm, // Получено
    on_pending, // В ожидании на проверку
    on_check, // В проверке
    checked // Проверно
}
