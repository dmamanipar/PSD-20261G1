package pe.edu.upeu;

public interface PasswordValidator {
    boolean isValid(String passw);
    int nivelSeguridad(String passw);
}
