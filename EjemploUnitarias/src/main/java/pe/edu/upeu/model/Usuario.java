package pe.edu.upeu.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class Usuario {
    private final String username;
    private final String email;
    private final String password;

}
