package pe.edu.upeu.repository;

import pe.edu.upeu.model.Usuario;

public interface UsuarioRepository {
    Usuario save(Usuario usuario);
    boolean existByEmail(String email);
}
