package pe.edu.upeu.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Usuario {
    private final String username;
    private final String email;
    private final String password;

}
