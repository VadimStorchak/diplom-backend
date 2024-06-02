package kubsu.ru.diplombackend.dto;

public enum LanguageEnum {
    java,
    R,
    python,
    Ruby,
    javascript;

    public String toString() {
        switch (this) {
            case java -> {
                return "java";
            }
            case R -> {
                return "R";
            }
            case python -> {
                return "python";
            }
            case Ruby -> {
                return "ruby";
            }
            case javascript -> {
                return "js";
            }
            default -> throw new RuntimeException("Неизвестный тип");
        }
    }
}
